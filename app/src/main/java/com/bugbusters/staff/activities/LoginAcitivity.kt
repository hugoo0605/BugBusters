package com.bugbusters.staff.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bugbusters.staff.activities.MainActivity
import com.bugbusters.staff.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(binding.root)
            setupUI()
        } catch (e: Exception) {
            Toast.makeText(this, "Error inicializando la app", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun setupUI() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email == "admin@bugbusters.com" && password == "1234") {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
            }
        }
    }
}