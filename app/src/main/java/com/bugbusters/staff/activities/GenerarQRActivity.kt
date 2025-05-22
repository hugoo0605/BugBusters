package com.bugbusters.staff.activities

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bugbusters.staff.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import java.io.File
import java.io.FileOutputStream

class GenerarQRActivity : AppCompatActivity() {

    private lateinit var qrBitmap: Bitmap
    private lateinit var mesaId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generar_qr)

        val inputText = findViewById<EditText>(R.id.editTextInput)
        val generateButton = findViewById<Button>(R.id.btnGenerateQR)
        val downloadPdfButton = findViewById<Button>(R.id.btnPrintQR)
        val qrImageView = findViewById<ImageView>(R.id.qrImageView)

        downloadPdfButton.visibility = View.GONE

        generateButton.setOnClickListener {
            mesaId = inputText.text.toString().trim()
            if (mesaId.isNotEmpty()) {
                val urlPersonalizada = "https://bugbusters-0jjv.onrender.com?$mesaId"
                generateQR(urlPersonalizada)?.let { bitmap ->
                    qrBitmap = bitmap
                    qrImageView.setImageBitmap(qrBitmap)
                    downloadPdfButton.visibility = View.VISIBLE
                } ?: run {
                    Toast.makeText(this, "Error generando QR", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Introduce el número de mesa", Toast.LENGTH_SHORT).show()
            }
        }

        downloadPdfButton.setOnClickListener {
            downloadQRasPdf()
        }
    }

    private fun generateQR(text: String): Bitmap? {
        val writer = QRCodeWriter()
        return try {
            val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(
                        x,
                        y,
                        if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE
                    )
                }
            }
            bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }

    private fun downloadQRasPdf() {
        if (!::qrBitmap.isInitialized) {
            Toast.makeText(this, "Primero genera el QR", Toast.LENGTH_SHORT).show()
            return
        }

        val width = 600
        val height = 800
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(width, height, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        val background = ContextCompat.getDrawable(this, R.drawable.background_gradient)
        background?.setBounds(0, 0, width, height)
        background?.draw(canvas)

        val paint = android.graphics.Paint()
        paint.color = android.graphics.Color.WHITE
        paint.textSize = 32f
        paint.isFakeBoldText = true

        val titulo = "Mesa $mesaId - Escaneá para ver el menú"
        val tituloWidth = paint.measureText(titulo)
        canvas.drawText(titulo, (width - tituloWidth) / 2f, 80f, paint)

        val qrSize = 300
        val qrLeft = (width - qrSize) / 2f
        val qrTop = 120f
        canvas.drawBitmap(
            qrBitmap,
            null,
            android.graphics.RectF(qrLeft, qrTop, qrLeft + qrSize, qrTop + qrSize),
            null
        )

        paint.textSize = 24f
        paint.isFakeBoldText = false

        val mensaje = "Gracias por visitarnos"
        val mensajeWidth = paint.measureText(mensaje)
        canvas.drawText(mensaje, (width - mensajeWidth) / 2f, 500f, paint)

        paint.textSize = 20f
        paint.color = android.graphics.Color.LTGRAY

        val web = "Bar Mirabel"
        val webWidth = paint.measureText(web)
        canvas.drawText(web, (width - webWidth) / 2f, 540f, paint)

        pdfDocument.finishPage(page)

        try {
            val file = File(getExternalFilesDir(null), "qr_mesa_$mesaId.pdf")
            val fos = FileOutputStream(file)
            pdfDocument.writeTo(fos)
            fos.close()
            Toast.makeText(this, "PDF guardado en: ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error guardando PDF: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            pdfDocument.close()
        }
    }


}
