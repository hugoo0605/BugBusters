package com.bugbusters.staff.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bugbusters.staff.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (validarCredenciales(email, password)) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validarCredenciales(email: String, password: String): Boolean {
        // Lógica de validación (ej: llamada a API)
        return email == "admin@bugbusters.com" && password == "1234"
    }
}