package com.bugbusters.staff.activities

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.bugbusters.staff.R

class GenerarQRActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generar_qr)

        val inputText = findViewById<EditText>(R.id.editTextInput)
        val generateButton = findViewById<Button>(R.id.btnGenerateQR)
        val qrImageView = findViewById<ImageView>(R.id.qrImageView)

        generateButton.setOnClickListener {
            val mesaId = inputText.text.toString().trim()
            if (mesaId.isNotEmpty()) {
                val urlPersonalizada = "https://bugbusters.com/mesa/$mesaId"
                val bitmap = generateQR(urlPersonalizada)
                qrImageView.setImageBitmap(bitmap)
            }
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
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
                }
            }
            bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }
}
