// src/main/java/com/bugbusters/staff/activities/MenuInicial.kt
package com.bugbusters.staff.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.bugbusters.staff.R

class MenuInicial : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_inicial)

        val btnPedidosActivos = findViewById<Button>(R.id.btnPedidosActivos)
        val btnGenerarQR = findViewById<Button>(R.id.btnGenerarQR)
        val btnGenerarCuentas = findViewById<Button>(R.id.btnGenerarCuentas)
        val btnFacturasPendientes = findViewById<Button>(R.id.btnFacturasPendientes)

        btnPedidosActivos.setOnClickListener {
            startActivity(Intent(this, PedidosActivosActivity::class.java))
        }

        btnGenerarQR.setOnClickListener {
            startActivity(Intent(this, GenerarQRActivity::class.java))
        }

        btnGenerarCuentas.setOnClickListener {
            startActivity(Intent(this, GenerarCuentaActivity::class.java))
        }

        btnFacturasPendientes.setOnClickListener {
            startActivity(Intent(this, FacturasPendientesActivity::class.java))
        }
    }
}
