package com.bugbusters.staff.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bugbusters.staff.adapters.ItemPedidoAdapter
import com.bugbusters.staff.api.PedidoApi
import com.bugbusters.staff.databinding.ActivityDetallePedidoBinding
import com.bugbusters.staff.dto.ItemPedidoDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetallePedidoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetallePedidoBinding
    private lateinit var api: PedidoApi
    private lateinit var adapter: ItemPedidoAdapter
    private var pedidoId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetallePedidoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pedidoId = intent.getLongExtra("pedido_id", -1)

        if (pedidoId == -1L) {
            Toast.makeText(this, "Pedido inválido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/api/")
            //.baseUrl("https://bugbustersspring.onrender.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(PedidoApi::class.java)

        cargarItemsDelPedido(pedidoId)
    }

    private fun cargarItemsDelPedido(pedidoId: Long) {
        binding.progressBarDetalle.visibility = View.VISIBLE

        api.obtenerItemsDePedido(pedidoId).enqueue(object : Callback<List<ItemPedidoDTO>> {
            override fun onResponse(
                call: Call<List<ItemPedidoDTO>>,
                response: Response<List<ItemPedidoDTO>>
            ) {
                binding.progressBarDetalle.visibility = View.GONE
                if (response.isSuccessful) {
                    val items = response.body().orEmpty()

                    adapter = ItemPedidoAdapter(items)
                    binding.listaProductosPedido.layoutManager =
                        LinearLayoutManager(this@DetallePedidoActivity)
                    binding.listaProductosPedido.adapter = adapter

                    setupBotones()

                } else {
                    Toast.makeText(
                        this@DetallePedidoActivity,
                        "Error al cargar productos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<ItemPedidoDTO>>, t: Throwable) {
                binding.progressBarDetalle.visibility = View.GONE
                Toast.makeText(this@DetallePedidoActivity, "Fallo: ${t.message}", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun setupBotones() {
        binding.btnFinalizarTodos.setOnClickListener {
            adapter.marcarTodosComoFinalizado()
        }

        binding.btnCancelar.setOnClickListener {
            finish()
        }

        binding.btnAceptar.setOnClickListener {
            val itemsActualizados = adapter.getItemsActualizados()
            var entregable = true

            for (item in itemsActualizados) {
                api.actualizarEstadoItem(item.id, item.estado)
                    .enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (!response.isSuccessful) {
                                val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                                Toast.makeText(
                                    this@DetallePedidoActivity,
                                    "Error al actualizar ${item.nombreProducto}: ${response.code()} - $errorMsg",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(
                                this@DetallePedidoActivity,
                                "Fallo: ${t.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })

                // Solo se finaliza el pedido si el estado de los productos es ENTREGADO o CANCELADO
                if (item.estado != "ENTREGADO" && item.estado != "CANCELADO") {
                    entregable = false
                }
            }

            if (entregable) {
                api.actualizarEstadoPedido(pedidoId, "ENTREGADO")
                    .enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    this@DetallePedidoActivity,
                                    "Pedido finalizado",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                                Toast.makeText(
                                    this@DetallePedidoActivity,
                                    "Error al actualizar pedido: ${response.code()} - $errorMsg",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(
                                this@DetallePedidoActivity,
                                "Fallo: ${t.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            } else {
                Toast.makeText(this, "Cambios aplicados", Toast.LENGTH_SHORT).show()
            }
            finish()
        }

        binding.btnCancelarPedido.setOnClickListener {
            api.actualizarEstadoPedido(pedidoId, "CANCELADO").enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@DetallePedidoActivity,
                            "Pedido cancelado correctamente",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                        Toast.makeText(
                            this@DetallePedidoActivity,
                            "Error al cancelar pedido: ${response.code()} - $errorMsg",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(
                        this@DetallePedidoActivity,
                        "Fallo: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}