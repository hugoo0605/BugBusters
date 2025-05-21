package com.bugbusters.staff.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bugbusters.staff.R
import com.bugbusters.staff.api.AuthApi
import com.bugbusters.staff.databinding.ActivityLoginBinding
import com.bugbusters.staff.dto.LoginRequest
import com.bugbusters.staff.dto.TrabajadorDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authApi: AuthApi

    //Boolean para el ojo de la contraseña
    private var isPasswordVisible = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/api/")
//            .baseUrl("https://bugbustersspring.onrender.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        authApi = retrofit.create(AuthApi::class.java)

        setupUI()
    }

    private fun setupUI() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            login(email, password)
        }
        // Ojo para mostrar/ocultar contraseña
        binding.showPasswordIcon.setOnClickListener {
            if (isPasswordVisible) {
                // Ocultar contraseña
                binding.passwordEditText.inputType =
                    android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.showPasswordIcon.setImageResource(R.drawable.ic_eye)
            } else {
                // Mostrar contraseña
                binding.passwordEditText.inputType =
                    android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.showPasswordIcon.setImageResource(R.drawable.ic_eye_off)
            }
            // Mover el cursor al final
            binding.passwordEditText.setSelection(binding.passwordEditText.text.length)
            isPasswordVisible = !isPasswordVisible
        }
    }

    private fun login(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)

        authApi.login(loginRequest).enqueue(object : Callback<TrabajadorDTO> {
            override fun onResponse(call: Call<TrabajadorDTO>, response: Response<TrabajadorDTO>) {
                if (response.isSuccessful) {
                    val trabajador = response.body()
                    if (trabajador != null) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Bienvenido, ${trabajador.nombre}",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@LoginActivity, MenuInicial::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Error inesperado", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Credenciales incorrectas",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<TrabajadorDTO>, t: Throwable) {
                Toast.makeText(
                    this@LoginActivity,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}
