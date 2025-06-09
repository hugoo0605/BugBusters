package com.bugbusters.staff.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bugbusters.staff.databinding.ActivityRegisterBinding
import com.bugbusters.staff.api.AuthApi
import okhttp3.ResponseBody
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Activity que gestiona el registro de nuevos usuarios.
 * Permite al usuario ingresar datos para crear una cuenta y realiza la llamada al API para registrar.
 */
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var authApi: AuthApi

    /**
     * Método del ciclo de vida onCreate.
     * Inicializa la vista, configura Retrofit y establece el listener para el botón de registro.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://bugbustersspring.onrender.com/api/")
            //.baseUrl("http://10.0.2.2:8080/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        authApi = retrofit.create(AuthApi::class.java)

        binding.btnRegistrarse.setOnClickListener {
            val dni = binding.etDni.text.toString().trim()
            val nombre = binding.etNombre.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val rol = binding.roleEditText.text.toString().trim()

            if (nombre.isEmpty() || dni.isEmpty() || email.isEmpty() || password.isEmpty() || rol.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val body = mapOf(
                "nombre" to nombre,
                "dni" to dni,
                "email" to email,
                "password" to password,
                "rol" to rol
            )

            authApi.register(body).enqueue(object : Callback<ResponseBody> {
                /**
                 * Callback para respuesta exitosa o error HTTP.
                 * Muestra mensaje correspondiente y redirige a Login en caso de éxito.
                 */
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@RegisterActivity, "Registro exitoso. Inicia sesión.", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@RegisterActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                /**
                 * Callback para fallo en la conexión o error de red.
                 * Muestra mensaje de error con la causa.
                 */
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, "Fallo: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}