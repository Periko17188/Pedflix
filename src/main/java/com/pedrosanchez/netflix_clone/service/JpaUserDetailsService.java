package com.pedrosanchez.netflix_clone.service;

import com.pedrosanchez.netflix_clone.model.User;
import com.pedrosanchez.netflix_clone.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

// Servicio que permite a Spring Security obtener la información de un usuario desde la base de datos
@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // Constructor con inyección de dependencias del repositorio de usuarios
    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Carga los datos de un usuario por su nombre de usuario
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);

        // Si el usuario no existe, lanza una excepción estándar de Spring Security
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        User user = userOptional.get();

        // Devuelve un objeto UserDetails con los datos que Spring Security necesita
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}