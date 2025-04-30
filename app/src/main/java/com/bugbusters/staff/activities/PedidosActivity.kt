package com.bugbusters.staff.activities

import com.bugbusters.staff.models.Pedido
import com.bugbusters.staff.models.ProductoPedido
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bugbusters.staff.adapters.PedidoAdapter
import com.bugbusters.staff.databinding.ActivityPedidosBinding

class PedidosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPedidosBinding
    private lateinit var pedidoAdapter: PedidoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedidosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mesaId = intent.getIntExtra("mesa_id", 0)
        val mesaNumero = intent.getStringExtra("mesa_numero") ?: ""

        binding.tvTituloMesa.text = "Pedidos - $mesaNumero"

        // Datos de prueba para el oepdido
        val pedido = Pedido(
            id = 1,
            mesaId = mesaId,
            productos = listOf(
                ProductoPedido("Coca-Cola", 2, 2.50),
                ProductoPedido("Ensalada César", 1, 8.50)
            ),
            estado = "PENDIENTE"
        )

        pedidoAdapter = PedidoAdapter(pedido.productos)
        binding.rvPedidos.apply {
            layoutManager = LinearLayoutManager(this@PedidosActivity)
            adapter = pedidoAdapter
        }

        binding.btnCobrar.setOnClickListener {
            Toast.makeText(this, "Cobrando mesa $mesaNumero", Toast.LENGTH_SHORT).show()
        }
    }
}