package com.bugbusters.staff.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bugbusters.staff.R
import com.bugbusters.staff.api.FacturaApi
import com.bugbusters.staff.dto.FacturaDTO
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class GenerarCuentaActivity : AppCompatActivity() {

    private lateinit var inputPedidoId: EditText
    private lateinit var generarFacturaBtn: Button
    private lateinit var facturaResultado: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generar_cuenta)

        inputPedidoId = findViewById(R.id.inputPedidoId)
        generarFacturaBtn = findViewById(R.id.generarFacturaBtn)
        facturaResultado = findViewById(R.id.facturaResultado)

        generarFacturaBtn.setOnClickListener {
            val pedidoId = inputPedidoId.text.toString().toLongOrNull()
            if (pedidoId != null) {
                generarFactura(pedidoId)
            } else {
                facturaResultado.text = "ID inválido"
            }
        }
    }

    private fun generarFactura(pedidoId: Long) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.100:8080/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(FacturaApi::class.java)

        api.generarFactura(pedidoId).enqueue(object : Callback<FacturaDTO> {
            override fun onResponse(call: Call<FacturaDTO>, response: Response<FacturaDTO>) {
                if (response.isSuccessful) {
                    val factura = response.body()
                    facturaResultado.text = """
                        Factura generada:
                        ID: ${factura?.id}
                        Total: ${factura?.total}€
                        Estado: ${factura?.estado}
                    """.trimIndent()
                } else {
                    facturaResultado.text = "Error al generar factura: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<FacturaDTO>, t: Throwable) {
                facturaResultado.text = "Error: ${t.message}"
            }
        })
    }
}
