package com.bugbusters.staff.activities

import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bugbusters.staff.R
import com.bugbusters.staff.api.PedidoApi
import com.bugbusters.staff.dto.ItemPedidoDTO
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import android.widget.ArrayAdapter

class DetallePedidoActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var api: PedidoApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_pedido)

        listView = findViewById(R.id.listaProductosPedido)
        progressBar = findViewById(R.id.progressBarDetalle)

        val pedidoId = intent.getLongExtra("pedido_id", -1)

        if (pedidoId == -1L) {
            Toast.makeText(this, "Pedido inválido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://bugbustersspring.onrender.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(PedidoApi::class.java)

        cargarItemsDelPedido(pedidoId)
    }

    private fun cargarItemsDelPedido(pedidoId: Long) {
        progressBar.visibility = View.VISIBLE

        api.obtenerItemsDePedido(pedidoId).enqueue(object : Callback<List<ItemPedidoDTO>> {
            override fun onResponse(call: Call<List<ItemPedidoDTO>>, response: Response<List<ItemPedidoDTO>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val items = response.body().orEmpty()
                    val adapter = ArrayAdapter(
                        this@DetallePedidoActivity,
                        android.R.layout.simple_list_item_1,
                        items.map {
                            "${it.cantidad} x ${it.nombreProducto} - ${it.precioUnitario}€ [${it.estado}]"
                        }
                    )
                    listView.adapter = adapter
                } else {
                    Toast.makeText(this@DetallePedidoActivity, "Error al cargar productos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ItemPedidoDTO>>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@DetallePedidoActivity, "Fallo: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
