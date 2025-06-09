package com.bugbusters.staff.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.bugbusters.staff.R

/**
 * Activity que muestra el menú principal tras iniciar sesión.
 * Permite acceder a diferentes funcionalidades como pedidos activos, generación de QR,
 * gestión de cuentas, facturas pendientes y cerrar sesión.
 */
class MenuInicial : AppCompatActivity() {

    /**
     * Método del ciclo de vida onCreate.
     * Inicializa los botones y configura sus listeners para navegar entre actividades.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_inicial)

        val btnPedidosActivos = findViewById<Button>(R.id.btnPedidosActivos)
        val btnGenerarQR = findViewById<Button>(R.id.btnGenerarQR)
        val btnGenerarCuentas = findViewById<Button>(R.id.btnGenerarCuentas)
        val btnFacturasPendientes = findViewById<Button>(R.id.btnFacturasPendientes)
        val btnCerrarSesion = findViewById<ImageButton>(R.id.btnCerrarSesion)

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

        btnCerrarSesion.setOnClickListener {
            val prefs = getSharedPreferences("bugbusters_prefs", MODE_PRIVATE)
            prefs.edit().clear().apply()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}