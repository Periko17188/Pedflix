package com.pedrosanchez.netflix_clone.service;

import com.pedrosanchez.netflix_clone.model.User;
import com.pedrosanchez.netflix_clone.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

// Componente de servicio requerido por Spring Security para cargar detalles del usuario.
@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // Inyección de dependencia del repositorio.
    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Busca un usuario por nombre en la base de datos para Spring Security.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);

        // Si el usuario no existe, lanza la excepción estándar de seguridad
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        User user = userOptional.get();

        // Construye el objeto UserDetails que Spring Security necesita
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}