package com.bugbusters.staff.activities

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
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

/**
 * Activity que gestiona la pantalla de login de la aplicación.
 * Permite al usuario iniciar sesión, recordar sesión y navegar al registro.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authApi: AuthApi
    private lateinit var rememberMeCheckBox: CheckBox

    /**
     * Booleano que indica si la contraseña está visible u oculta.
     */
    private var isPasswordVisible = false


    /**
     * Método del ciclo de vida de la Activity.
     * Comprueba si existe sesión guardada y redirige directamente al menú principal si es así.
     * Si no, inicializa la UI y configura Retrofit.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("bugbusters_prefs", MODE_PRIVATE)
        val userId = prefs.getLong("user_id", -1L)
        if (userId != -1L) {
            val intent = Intent(this, MenuInicial::class.java)
            startActivity(intent)
            finish()
            return
        }

        // Si no hay sesión guardada configyra UI normalmente
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar Retrofit
        val retrofit = Retrofit.Builder()
            //.baseUrl("http://10.0.2.2:8080/api/")
            .baseUrl("https://bugbustersspring.onrender.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        authApi = retrofit.create(AuthApi::class.java)
        rememberMeCheckBox = binding.rememberMeCheckBox

        setupUI()
    }

    /**
     * Configura los listeners de la interfaz, incluyendo:
     * - Botón de login para validar campos y ejecutar login.
     * - Icono para mostrar/ocultar contraseña.
     * - Texto para navegar a la pantalla de registro.
     */
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
        binding.goToRegisterText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Ejecuta la llamada a la API para autenticar al usuario con email y contraseña.
     * Si el login es exitoso, guarda la sesión si está seleccionado y navega al menú inicial.
     *
     * @param email Correo electrónico introducido por el usuario.
     * @param password Contraseña introducida por el usuario.
     */
    private fun login(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)

        authApi.login(loginRequest).enqueue(object : Callback<TrabajadorDTO> {
            override fun onResponse(call: Call<TrabajadorDTO>, response: Response<TrabajadorDTO>) {
                if (response.isSuccessful) {
                    val trabajador = response.body()
                    if (trabajador != null) {
                        if (rememberMeCheckBox.isChecked) {
                            val prefs = getSharedPreferences("bugbusters_prefs", MODE_PRIVATE)
                            with(prefs.edit()) {
                                putLong("user_id", trabajador.id)
                                putString("user_name", trabajador.nombre)
                                putString("user_email", trabajador.email)
                                apply()
                            }
                        }
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

            /**
             * Muestra un mensaje de error en caso de fallo de conexión o error en la llamada a la API.
             */
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