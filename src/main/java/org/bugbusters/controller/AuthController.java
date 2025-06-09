package org.bugbusters.controller;

import org.bugbusters.entity.Trabajador;
import org.bugbusters.repository.TrabajadorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador para el inicio de sesión y el registro de nuevos trabajadores.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final TrabajadorRepository trabajadorRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor que inyecta las dependencias necesarias para la autenticación.
     *
     * @param trabajadorRepository Repositorio para acceder a los datos de los trabajadores.
     * @param passwordEncoder      Codificador de contraseñas para cifrado y verificación segura.
     */
    public AuthController(TrabajadorRepository trabajadorRepository, PasswordEncoder passwordEncoder) {
        this.trabajadorRepository = trabajadorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Endpoint para iniciar sesión.
     * Verifica las credenciales proporcionadas por el usuario y devuelve datos básicos si son correctas.
     *
     * @param loginRequest Mapa con las claves "email" y "password".
     * @return ResponseEntity con mensaje de éxito y datos del trabajador, o error si las credenciales son incorrectas.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        Optional<Trabajador> trabajadorOpt = trabajadorRepository.findByEmail(email);

        if (trabajadorOpt.isPresent()) {
            Trabajador trabajador = trabajadorOpt.get();
            if (passwordEncoder.matches(password, trabajador.getPassword())) {
                return ResponseEntity.ok(Map.of(
                        "message", "Login correcto",
                        "rol", trabajador.getRol(),
                        "nombre", trabajador.getNombre(),
                        "id", trabajador.getId()
                ));
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Credenciales incorrectas"));
    }

    /**
     * Endpoint para registrar un nuevo trabajador.
     * Crea un nuevo usuario si el email no está ya registrado.
     *
     * @param registerRequest Mapa con las claves dni, nombre, email, password y rol.
     * @return ResponseEntity con mensaje de éxito y datos del nuevo usuario, o error si ya existe un usuario con ese email.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> registerRequest) {
        String dni = registerRequest.get("dni");
        String nombre = registerRequest.get("nombre");
        String email = registerRequest.get("email");
        String password = registerRequest.get("password");
        String rol = registerRequest.getOrDefault("rol", "empleado");

        if (trabajadorRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Ya existe un usuario con ese email"));
        }

        Trabajador nuevo = new Trabajador();
        nuevo.setDni(dni);
        nuevo.setNombre(nombre);
        nuevo.setEmail(email);
        nuevo.setPassword(passwordEncoder.encode(password));
        nuevo.setRol(rol);
        nuevo.setActivo(true);
        nuevo.setFechaCreacion(LocalDateTime.now());

        trabajadorRepository.save(nuevo);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Usuario registrado correctamente",
                "id", nuevo.getId(),
                "nombre", nuevo.getNombre(),
                "email", nuevo.getEmail()
        ));
    }
}