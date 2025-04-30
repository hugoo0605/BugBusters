package com.bugbusters.staff.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bugbusters.staff.activities.PedidosActivity
import com.bugbusters.staff.adapters.MesaAdapter
import com.bugbusters.staff.databinding.ActivityMainBinding
import com.bugbusters.staff.models.Mesa

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mesas = listOf(
            Mesa(1, "Mesa 1", "OCUPADA"),
            Mesa(2, "Mesa 2", "LIBRE"),
            Mesa(3, "Mesa 3", "RESERVADA")
        )

        binding.rvMesas.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = MesaAdapter(mesas) { mesa ->
                val intent = Intent(this@MainActivity, PedidosActivity::class.java).apply {
                    putExtra("mesa_id", mesa.id)
                    putExtra("mesa_numero", mesa.numero)
                }
                startActivity(intent)
            }
        }
    }
}