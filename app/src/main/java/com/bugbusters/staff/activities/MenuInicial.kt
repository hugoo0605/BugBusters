package com.bugbusters.staff.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.bugbusters.staff.R

class MenuInicial : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_inicial)

        val btnPedidosActivos = findViewById<Button?>(R.id.btnPedidosActivos)
        val btnGenerarQR = findViewById<Button?>(R.id.btnGenerarQR)
        val btnGenerarCuentas = findViewById<Button?>(R.id.btnGenerarCuentas)

        btnPedidosActivos.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(this, PedidosActivosActivity::class.java)
            startActivity(intent)
        })

        btnGenerarQR.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(this, GenerarQRActivity::class.java)
            startActivity(intent)
        })

        btnGenerarCuentas.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(this, GenerarCuentaActivity::class.java)
            startActivity(intent)
        })
    }
}