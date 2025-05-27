package com.bugbusters.staff.activities

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bugbusters.staff.R
import com.bugbusters.staff.adapters.MesaAdapter
import com.bugbusters.staff.dto.MesaDTO
import com.bugbusters.staff.dto.SesionMesaDTO
import com.bugbusters.staff.network.RetrofitInstance
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class GenerarQRActivity : AppCompatActivity() {

    private lateinit var qrImageView: ImageView
    private lateinit var rvMesas: RecyclerView
    private lateinit var btnGuardarPdf: Button

    private var qrBitmap: Bitmap? = null
    private var sesionUUID: UUID? = null
    private var mesaSeleccionadaId: Long? = null
    private var isDeleteMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generar_qr)

        qrImageView = findViewById(R.id.qrImageView)
        rvMesas = findViewById(R.id.recyclerMesas)
        val fabAgregarMesa: View = findViewById(R.id.fabAgregarMesa)
        val fabEliminarMesa: FloatingActionButton = findViewById(R.id.fabEliminarMesa)
        btnGuardarPdf = findViewById(R.id.btnGuardarPdf)

        btnGuardarPdf.visibility = View.GONE

        // Configuramos el RecyclerView
        rvMesas.layoutManager = LinearLayoutManager(this)
        lateinit var adapter: MesaAdapter

        adapter = MesaAdapter(
            onMesaClick = { mesaId ->
                if (!isDeleteMode) {
                    mesaSeleccionadaId = mesaId
                    obtenerIdSesionYGenerarQR(mesaId)
                }
            },
            onDeleteMesa = { mesaId ->
                eliminarMesa(mesaId, adapter)
            },
            isDeleteMode = isDeleteMode
        )
        rvMesas.adapter = adapter
        cargarMesas(adapter)

        fabAgregarMesa.setOnClickListener {
            crearNuevaMesa(adapter)
        }
        fabEliminarMesa.setOnClickListener {
            isDeleteMode = !isDeleteMode
            adapter.setDeleteMode(isDeleteMode)
            // Cambiar color del FAB para feedback visual
            fabEliminarMesa.backgroundTintList = ColorStateList.valueOf(
                if (isDeleteMode) Color.RED else ContextCompat.getColor(this, R.color.purple_500)
            )
        }

        btnGuardarPdf.setOnClickListener {
            sesionUUID?.let { uuid ->
                downloadQRasPdf(uuid)
            }
        }
    }

    private fun eliminarMesa(mesaId: Long, adapter: MesaAdapter) {
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.mesaApi.eliminarMesa(mesaId)
                val fabEliminarMesa: FloatingActionButton = findViewById(R.id.fabEliminarMesa)
                if (response.isSuccessful) {
                    isDeleteMode = false
                    adapter.setDeleteMode(false)
                    cargarMesas(adapter)
                    fabEliminarMesa.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(this@GenerarQRActivity, R.color.purple_500)
                    )
                    Toast.makeText(this@GenerarQRActivity, "Mesa eliminada", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@GenerarQRActivity, "Error eliminando mesa", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@GenerarQRActivity, "Error de red al eliminar mesa", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cargarMesas(adapter: MesaAdapter) {
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.mesaApi.getMesas()
                if (response.isSuccessful) {
                    val mesas = response.body().orEmpty()
                    adapter.actualizarMesas(mesas)
                } else {
                    Toast.makeText(this@GenerarQRActivity, "Error cargando mesas", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@GenerarQRActivity, "Error de red al cargar mesas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun crearNuevaMesa(adapter: MesaAdapter) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_nueva_mesa, null)
        val etNumero = dialogView.findViewById<EditText>(R.id.etNumeroMesa)
        val etEstado = dialogView.findViewById<EditText>(R.id.etEstadoMesa)
        val etCapacidad = dialogView.findViewById<EditText>(R.id.etCapacidadMesa)
        val etUbicacion = dialogView.findViewById<EditText>(R.id.etUbicacionMesa)

        AlertDialog.Builder(this)
            .setTitle("Crear nueva mesa")
            .setView(dialogView)
            .setPositiveButton("Crear") { _, _ ->
                val numero = etNumero.text.toString().toIntOrNull()
                val estado = etEstado.text.toString().trim()
                val capacidad = etCapacidad.text.toString().toIntOrNull()
                val ubicacion = etUbicacion.text.toString().trim()

                if (numero != null && capacidad != null && estado.isNotEmpty() && ubicacion.isNotEmpty()) {
                    lifecycleScope.launch {
                        val nuevaMesa = MesaDTO(
                            numero = numero,
                            estado = estado,
                            capacidad = capacidad,
                            ubicacion = ubicacion
                        )
                        val response = RetrofitInstance.mesaApi.crearMesa(nuevaMesa)
                        if (response.isSuccessful) {
                            cargarMesas(adapter)
                            Toast.makeText(this@GenerarQRActivity, "Mesa creada", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@GenerarQRActivity, "Error creando mesa", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun obtenerIdSesionYGenerarQR(mesaId: Long) {
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.sesionMesaApi.obtenerSesionesPorMesa(mesaId)
                if (response.isSuccessful) {
                    val sesiones: List<SesionMesaDTO> = response.body().orEmpty()
                    if (sesiones.isNotEmpty()) {
                        val primeraSesion = sesiones[0]
                        sesionUUID = primeraSesion.id

                        val urlParaQR = "https://bugbusters-0jjv.onrender.com?mesa=${sesionUUID}"
                        generateQR(urlParaQR)?.let { bitmap ->
                            qrBitmap = bitmap
                            mostrarQRPopup(bitmap)
                            btnGuardarPdf.visibility = View.VISIBLE
                        }
                    } else {
                        Toast.makeText(this@GenerarQRActivity, "No se encontró ninguna sesión para esa mesa", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@GenerarQRActivity, "Error al obtener sesiones de la mesa", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@GenerarQRActivity, "Error de red al obtener sesión", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generateQR(text: String): Bitmap? {
        return try {
            val bitMatrix = QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, 512, 512)
            val bitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.RGB_565)
            for (x in 0 until 512) {
                for (y in 0 until 512) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }

    private fun mostrarQRPopup(qrBitmap: Bitmap) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_qr_popup, null)
        val imageView = dialogView.findViewById<ImageView>(R.id.ivQRPopup)
        val btnGuardar = dialogView.findViewById<Button>(R.id.btnGuardarPdfPopup)

        imageView.setImageBitmap(qrBitmap)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        btnGuardar.setOnClickListener {
            sesionUUID?.let { uuid ->
                downloadQRasPdf(uuid)
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun downloadQRasPdf(uuid: UUID) {
        qrBitmap?.let { bitmap ->
            val width = 600
            val height = 800
            val document = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(width, height, 1).create()
            val page = document.startPage(pageInfo)
            val canvas: Canvas = page.canvas

            val background = ContextCompat.getDrawable(this, R.drawable.background_gradient)
            background?.setBounds(0, 0, width, height)
            background?.draw(canvas)

            val paint = android.graphics.Paint()
            paint.color = Color.WHITE
            paint.textSize = 32f
            paint.isFakeBoldText = true

            val titulo = "Mesa $mesaSeleccionadaId - Escaneá para ver el menú"
            val tituloWidth = paint.measureText(titulo)
            canvas.drawText(titulo, (width - tituloWidth) / 2f, 80f, paint)

            val qrSize = 300
            val qrLeft = (width - qrSize) / 2f
            val qrTop = 120f
            canvas.drawBitmap(bitmap, null, android.graphics.RectF(qrLeft, qrTop, qrLeft + qrSize, qrTop + qrSize), null)

            paint.textSize = 24f
            paint.isFakeBoldText = false
            val mensaje = "Gracias por visitarnos"
            val mensajeWidth = paint.measureText(mensaje)
            canvas.drawText(mensaje, (width - mensajeWidth) / 2f, 500f, paint)

            paint.textSize = 20f
            paint.color = Color.LTGRAY
            val web = "Bar Mirabel"
            val webWidth = paint.measureText(web)
            canvas.drawText(web, (width - webWidth) / 2f, 540f, paint)

            document.finishPage(page)

            val fileName = "qr_mesa_${uuid}.pdf"
            val file = File(getExternalFilesDir(null), fileName)

            try {
                val outputStream = FileOutputStream(file)
                document.writeTo(outputStream)
                outputStream.close()
                Toast.makeText(this, "PDF guardado: ${file.absolutePath}", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error al guardar PDF", Toast.LENGTH_SHORT).show()
            } finally {
                document.close()
            }
        }
    }
}
