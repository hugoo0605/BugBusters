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

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final TrabajadorRepository trabajadorRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(TrabajadorRepository trabajadorRepository, PasswordEncoder passwordEncoder) {
        this.trabajadorRepository = trabajadorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        Optional<Trabajador> trabajadorOpt = trabajadorRepository.findByEmail(email);

        if (trabajadorOpt.isPresent()) {
            Trabajador trabajador = trabajadorOpt.get();
            if (passwordEncoder.matches(password, trabajador.getPassword())) {
                // Aquí podrías generar un token JWT o devolver los datos básicos
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
}

