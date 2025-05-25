package com.bugbusters.staff.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bugbusters.staff.R
import com.bugbusters.staff.api.PedidoApi
import com.bugbusters.staff.dto.PedidoDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PedidosActivosActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var api: PedidoApi

    //Refresca la pagina cada x tiempo
    private val refreshHandler = Handler(Looper.getMainLooper())
    private lateinit var refreshRunnable: Runnable
    private val refreshInterval: Long = 5000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedidos_activos)

        listView = findViewById(R.id.listaPedidos)
        progressBar = findViewById(R.id.progressBar)

        // Configurar Retrofit
        val retrofit = Retrofit.Builder()
            //  .baseUrl("http://10.0.2.2:8080/api/")
            .baseUrl("https://bugbustersspring.onrender.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(PedidoApi::class.java)

        cargarPedidosActivos()

        refreshRunnable = Runnable {
            cargarPedidosActivos()
            refreshHandler.postDelayed(refreshRunnable, refreshInterval)
        }
    }

    private var esPrimeraCarga = true

    private fun cargarPedidosActivos() {
        if (esPrimeraCarga) {
            progressBar.visibility = View.VISIBLE
        }

        api.obtenerPedidosActivos().enqueue(object : Callback<List<PedidoDTO>> {
            override fun onResponse(
                call: Call<List<PedidoDTO>>,
                response: Response<List<PedidoDTO>>
            ) {
                if (esPrimeraCarga) {
                    progressBar.visibility = View.GONE
                    esPrimeraCarga = false
                }
                if (response.isSuccessful) {
                    val pedidos = response.body().orEmpty()
                    val adapter = ArrayAdapter(
                        this@PedidosActivosActivity,
                        R.layout.item_pedido_blanco,
                        pedidos.map { "Mesa ${it.mesa} - Total: ${it.total}€ - Estado: ${it.estado}" }
                    )
                    listView.adapter = adapter
                    listView.setOnItemClickListener { _, _, position, _ ->
                        val pedidoSeleccionado = pedidos[position]
                        val intent =
                            Intent(this@PedidosActivosActivity, DetallePedidoActivity::class.java)
                        intent.putExtra("pedido_id", pedidoSeleccionado.id)
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(
                        this@PedidosActivosActivity,
                        "Error al obtener pedidos: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<PedidoDTO>>, t: Throwable) {
                if (esPrimeraCarga) {
                    progressBar.visibility = View.GONE
                    esPrimeraCarga = false
                }
                Toast.makeText(
                    this@PedidosActivosActivity,
                    "Fallo: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }


    override fun onResume() {
        super.onResume()
        // Inicia el refresco automático cuando se muestra la pantalla
        refreshHandler.postDelayed(refreshRunnable, refreshInterval)
    }

    override fun onPause() {
        super.onPause()
        // Detiene el refresco automático cuando se oculta la pantalla
        refreshHandler.removeCallbacks(refreshRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Por seguridad, quitamos cualquier callback pendiente al destruir
        refreshHandler.removeCallbacks(refreshRunnable)
    }
}
