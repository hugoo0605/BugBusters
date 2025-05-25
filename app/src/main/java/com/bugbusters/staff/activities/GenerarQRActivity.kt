package com.bugbusters.staff.activities

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color // ✅ import corregido
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bugbusters.staff.R
import com.bugbusters.staff.adapters.MesaAdapter
import com.bugbusters.staff.dto.MesaDTO
import com.bugbusters.staff.network.RetrofitInstance // ✅ import para mesaApi
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class GenerarQRActivity : AppCompatActivity() {

    private lateinit var qrImageView: ImageView
    private lateinit var rvMesas: RecyclerView
    private lateinit var btnGuardarPdf: Button

    private var qrBitmap: Bitmap? = null
    private var mesaSeleccionadaId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generar_qr)

        qrImageView = findViewById(R.id.qrImageView)
        rvMesas = findViewById(R.id.recyclerMesas)
        val fabAgregarMesa: View = findViewById(R.id.fabAgregarMesa)
        btnGuardarPdf = findViewById(R.id.btnGuardarPdf)

        btnGuardarPdf.visibility = View.GONE
        rvMesas.layoutManager = LinearLayoutManager(this)

        val adapter = MesaAdapter { mesaId ->
            mesaSeleccionadaId = mesaId
            val url = "https://bugbusters-0jjv.onrender.com?$mesaId"
            generateQR(url)?.let {
                qrBitmap = it
                qrImageView.setImageBitmap(it)
                btnGuardarPdf.visibility = View.VISIBLE
            }
        }
        rvMesas.adapter = adapter

        cargarMesas(adapter)

        fabAgregarMesa.setOnClickListener {
            crearNuevaMesa(adapter)
        }

        btnGuardarPdf.setOnClickListener {
            mesaSeleccionadaId?.let { id -> downloadQRasPdf(id) }
        }
    }

    private fun cargarMesas(adapter: MesaAdapter) {
        lifecycleScope.launch {
            val response = RetrofitInstance.mesaApi.getMesas()
            if (response.isSuccessful) {
                adapter.actualizarMesas(response.body().orEmpty())
            } else {
                Toast.makeText(this@GenerarQRActivity, "Error cargando mesas", Toast.LENGTH_SHORT).show()
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

    private fun downloadQRasPdf(mesaId: Long) {
        qrBitmap?.let { bitmap ->
            val document = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
            val page = document.startPage(pageInfo)
            val canvas: Canvas = page.canvas
            canvas.drawBitmap(bitmap, 0f, 0f, null)
            document.finishPage(page)

            val fileName = "qr_mesa_$mesaId.pdf"
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
