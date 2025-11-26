package com.pedrosanchez.netflix_clone.service;

import com.pedrosanchez.netflix_clone.model.User;
import com.pedrosanchez.netflix_clone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Buscar usuario en BD
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        User user = userOptional.get();

        // Convertir roles (String) → GrantedAuthority
        Set<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> {
                    // Asegurar formato ROLE_XXXX
                    if (!role.startsWith("ROLE_")) {
                        role = "ROLE_" + role;
                    }
                    return new SimpleGrantedAuthority(role);
                })
                .collect(Collectors.toSet());

        // Crear un UserDetails válido para Spring Security
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}
