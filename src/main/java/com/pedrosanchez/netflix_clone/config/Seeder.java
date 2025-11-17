package com.pedrosanchez.netflix_clone.config;

import com.pedrosanchez.netflix_clone.model.User;
import com.pedrosanchez.netflix_clone.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class Seeder {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            final String rawPassword = "12341234";
            final String encodedPassword = passwordEncoder.encode(rawPassword);

            // ADMIN
            userRepository.findByUsername("Pedro").ifPresentOrElse(user -> {
                user.setPassword(encodedPassword);
                user.setRole("ADMIN");
                userRepository.save(user);
            }, () -> {
                userRepository.save(new User("Pedro", encodedPassword, "ADMIN"));
            });

            System.out.println("Seeder actualizado: Pedro=ADMIN");
        };
    }
}
