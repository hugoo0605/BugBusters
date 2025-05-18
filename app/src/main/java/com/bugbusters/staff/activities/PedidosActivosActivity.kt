package com.bugbusters.staff.activities

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bugbusters.staff.R
import com.bugbusters.staff.api.PedidoApi
import com.bugbusters.staff.dto.PedidoDTO
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class PedidosActivosActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var api: PedidoApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedidos_activos)

        listView = findViewById(R.id.listaPedidos)
        progressBar = findViewById(R.id.progressBar)

        // Configurar Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dpg-d0ctusidbo4c73frinng-a.frankfurt-postgres.render.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(PedidoApi::class.java)

        cargarPedidosActivos()
    }

    private fun cargarPedidosActivos() {
        progressBar.visibility = View.VISIBLE

        api.obtenerPedidosActivos().enqueue(object : Callback<List<PedidoDTO>> {
            override fun onResponse(call: Call<List<PedidoDTO>>, response: Response<List<PedidoDTO>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val pedidos = response.body().orEmpty()
                    val adapter = ArrayAdapter(
                        this@PedidosActivosActivity,
                        android.R.layout.simple_list_item_1,
                        pedidos.map { "Mesa ${it.mesa} - Total: ${it.total}€ - Estado: ${it.estado}" }
                    )
                    listView.adapter = adapter
                } else {
                    Toast.makeText(
                        this@PedidosActivosActivity,
                        "Error al obtener pedidos: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<PedidoDTO>>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@PedidosActivosActivity, "Fallo: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
