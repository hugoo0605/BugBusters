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

/**
 * Actividad para generar una factura a partir del número de mesa.
 */
class GenerarCuentaActivity : AppCompatActivity() {

    // Campo de texto para introducir el número de mesa
    private lateinit var inputMesaId: EditText

    // Botón para generar la factura
    private lateinit var generarFacturaBtn: Button

    // TextView para mostrar el resultado de la factura
    private lateinit var facturaResultado: TextView

    /**
     * Inicializa la actividad y sus componentes.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generar_cuenta)

        inputMesaId = findViewById(R.id.inputPedidoId)
        generarFacturaBtn = findViewById(R.id.generarFacturaBtn)
        facturaResultado = findViewById(R.id.facturaResultado)

        generarFacturaBtn.setOnClickListener {
            val numeroMesa = inputMesaId.text.toString().toIntOrNull()
            if (numeroMesa != null) {
                generarFacturaParaMesa(numeroMesa)
            } else {
                facturaResultado.text = "ID de mesa inválido"
            }
        }
    }

    /**
     * Llama a la API para generar la factura de la mesa indicada.
     *
     * @param numeroMesa Número de la mesa a facturar.
     */
    private fun generarFacturaParaMesa(numeroMesa: Int) {
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.facturaApi.generarFacturaPorNumeroMesa(numeroMesa)
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

    /**
     * Muestra la factura generada en pantalla.
     *
     * @param factura Factura recibida de la API.
     */
    @SuppressLint("SetTextI18n")
    private fun mostrarFactura(factura: FacturaDTO) {
        val pedidosStr = factura.pedidoIds.joinToString(", ")
        facturaResultado.text = """
            ✅ Factura Generada
            ID: ${factura.id}
            Estado: ${factura.estado}
            Fecha: ${factura.fecha}
            Total: ${factura.total} €
            Pedidos facturados: [$pedidosStr]
        """.trimIndent()
    }
}