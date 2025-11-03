package com.pedrosanchez.netflix_clone.controller;

import com.pedrosanchez.netflix_clone.model.User;
import com.pedrosanchez.netflix_clone.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controlador para gestionar el registro y la autenticación de usuarios
@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Inyección de dependencias
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Registra un nuevo usuario
    @PostMapping("/registro")
    public ResponseEntity<User> registerUser(@RequestBody User user) {

        // Verifica si el nombre de usuario ya existe
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Codifica la contraseña antes de guardar
        String rawPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);

        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(encodedPassword);
        newUser.setRole("USER");

        User savedUser = userRepository.save(newUser);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }
}
