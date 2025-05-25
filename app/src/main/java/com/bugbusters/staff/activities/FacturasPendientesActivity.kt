package com.bugbusters.staff.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bugbusters.staff.R
import com.bugbusters.staff.adapters.FacturaAdapter
import com.bugbusters.staff.dto.FacturaDTO
import com.bugbusters.staff.network.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class FacturasPendientesActivity : AppCompatActivity() {

    private lateinit var rvFacturas: RecyclerView
    private lateinit var tvSinFacturas: View
    private lateinit var adapter: FacturaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facturas_pendientes)

        rvFacturas = findViewById(R.id.rvFacturasPendientes)
        tvSinFacturas = findViewById(R.id.tvSinFacturas)

        rvFacturas.layoutManager = LinearLayoutManager(this)
        adapter = FacturaAdapter(mutableListOf()) { factura ->
            marcarFacturaPagada(factura)
        }
        rvFacturas.adapter = adapter

        cargarFacturasPendientes()
    }

    private fun cargarFacturasPendientes() {
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.facturaApi.getFacturasPendientes()
                if (response.isSuccessful) {
                    val lista = response.body().orEmpty()
                    if (lista.isEmpty()) {
                        rvFacturas.visibility = View.GONE
                        tvSinFacturas.visibility = View.VISIBLE
                    } else {
                        rvFacturas.visibility = View.VISIBLE
                        tvSinFacturas.visibility = View.GONE
                        adapter.actualizarLista(lista)
                    }
                } else {
                    Toast.makeText(
                        this@FacturasPendientesActivity,
                        "Error servidor: ${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: IOException) {
                Toast.makeText(
                    this@FacturasPendientesActivity,
                    "Error red: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
                Log.e("FacturasPendientes", "IOException", e)
            } catch (e: HttpException) {
                Toast.makeText(
                    this@FacturasPendientesActivity,
                    "Error HTTP: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
                Log.e("FacturasPendientes", "HttpException", e)
            } catch (e: Exception) {
                Toast.makeText(
                    this@FacturasPendientesActivity,
                    "Error inesperado: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
                Log.e("FacturasPendientes", "Exception", e)
            }
        }
    }

    private fun marcarFacturaPagada(factura: FacturaDTO) {
        // Mostrar diálogo de confirmación
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Confirmar pago")
            .setMessage("¿Estás seguro de que quieres marcar la factura ${factura.id} como pagada?")
            .setPositiveButton("Sí") { _, _ ->
                // Si el usuario confirma, se marca como pagada
                lifecycleScope.launch {
                    try {
                        val response = RetrofitInstance.facturaApi.pagarFactura(factura.id)
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@FacturasPendientesActivity,
                                "✅ Factura ${factura.id} marcada como pagada",
                                Toast.LENGTH_SHORT
                            ).show()
                            cargarFacturasPendientes()
                        } else {
                            Toast.makeText(
                                this@FacturasPendientesActivity,
                                "❌ Error marcando como pagada: ${response.code()}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } catch (e: IOException) {
                        Toast.makeText(
                            this@FacturasPendientesActivity,
                            "Error de red: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.e("FacturasPendientes", "IOException", e)
                    } catch (e: HttpException) {
                        Toast.makeText(
                            this@FacturasPendientesActivity,
                            "Error HTTP: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.e("FacturasPendientes", "HttpException", e)
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@FacturasPendientesActivity,
                            "Error inesperado: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.e("FacturasPendientes", "Exception", e)
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }
}