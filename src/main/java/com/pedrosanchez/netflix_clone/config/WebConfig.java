package com.pedrosanchez.netflix_clone.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Clase de configuración general del proyecto para habilitar CORS
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Permite que el frontend pueda hacer peticiones al backend sin errores de origen cruzado
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "https://localhost:8443",
                        "https://127.0.0.1:8443"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}