package com.pedrosanchez.netflix_clone.config;

import com.pedrosanchez.netflix_clone.service.JpaUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// Clase de configuración de seguridad para la aplicación.
// Aquí se definen los permisos, el login, el logout y la codificación de contraseñas.
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JpaUserDetailsService jpaUserDetailsService;

    // Constructor que recibe el servicio encargado de cargar los usuarios
    public SecurityConfig(JpaUserDetailsService jpaUserDetailsService) {
        this.jpaUserDetailsService = jpaUserDetailsService;
    }

    // Configura las reglas de seguridad y las rutas protegidas
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .userDetailsService(jpaUserDetailsService)
                .cors(Customizer.withDefaults())
                // Deshabilitar CSRF para la API y H2 Console
                .csrf(csrf -> csrf.ignoringRequestMatchers(
                        "/h2-console/**",
                        "/api/v1/registro"
                    )
                )

                // Permite que la consola H2 funcione dentro de un iframe
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )
                .authorizeHttpRequests(auth -> auth
                    // Rutas públicas
                    .requestMatchers("/", "/index.html", "/static/**", "/css/**", "/js/**", "/images/**",
                    "/h2-console/**").permitAll()
                    // Permitir registro sin autenticación
                    .requestMatchers(HttpMethod.POST, "/api/v1/registro").permitAll()
                    // Permitir acceso a películas y géneros sin autenticación
                    .requestMatchers(HttpMethod.GET, "/api/v1/peliculas", "/api/v1/generos").permitAll()
                    // Rutas que requieren rol ADMIN
                    .requestMatchers(HttpMethod.POST, "/api/v1/peliculas").hasRole("ADMIN")
                    // Cualquier otra ruta requiere autenticación
                    .anyRequest().authenticated()
                )
                // Configura el formulario de inicio de sesión
                .formLogin(form -> form.loginPage("/login").permitAll()
                .defaultSuccessUrl("/", true))

                // Configura el cierre de sesión
                .logout(logout -> logout.permitAll().logoutSuccessUrl("/?logout"))
                .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }

    // Codifica las contraseñas con BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
