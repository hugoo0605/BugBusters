package com.bugbusters.staff.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bugbusters.staff.R
import com.bugbusters.staff.dto.FacturaDTO
import com.bugbusters.staff.network.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class GenerarCuentaActivity : AppCompatActivity() {

    private lateinit var inputMesaId: EditText
    private lateinit var generarFacturaBtn: Button
    private lateinit var facturaResultado: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generar_cuenta)

        inputMesaId = findViewById(R.id.inputPedidoId)
        generarFacturaBtn = findViewById(R.id.generarFacturaBtn)
        facturaResultado = findViewById(R.id.facturaResultado)

        generarFacturaBtn.setOnClickListener {
            val mesaId = inputMesaId.text.toString().toLongOrNull()
            if (mesaId != null) {
                generarFacturaParaMesa(mesaId)
            } else {
                facturaResultado.text = "ID de mesa inválido"
            }
        }
    }

    private fun generarFacturaParaMesa(mesaId: Long) {
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.facturaApi.generarFacturaPorMesa(mesaId)
                if (response.isSuccessful) {
                    response.body()?.let { factura ->
                        mostrarFactura(factura)
                    } ?: run {
                        facturaResultado.text = "Factura vacía"
                    }
                } else {
                    facturaResultado.text = "Error del servidor: ${response.code()}"
                }
            } catch (e: IOException) {
                facturaResultado.text = "Error de red: ${e.message}"
                Log.e("Factura", "IOException", e)
            } catch (e: HttpException) {
                facturaResultado.text = "Error HTTP: ${e.message}"
                Log.e("Factura", "HttpException", e)
            } catch (e: Exception) {
                facturaResultado.text = "Error inesperado: ${e.message}"
                Log.e("Factura", "Exception", e)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun mostrarFactura(factura: FacturaDTO) {
        facturaResultado.text = """
            ✅ Factura Generada
            ID: ${factura.id}
            Estado: ${factura.estado}
            Fecha: ${factura.fecha}
            Total: ${factura.montoTotal} €
            Pedidos: ${factura.pedidos.joinToString()}
        """.trimIndent()
    }
}
