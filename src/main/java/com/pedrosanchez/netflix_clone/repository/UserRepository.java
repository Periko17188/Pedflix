package com.pedrosanchez.netflix_clone.repository;

import com.pedrosanchez.netflix_clone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// Repositorio que gestiona las operaciones CRUD para la entidad User
public interface UserRepository extends JpaRepository<User, Long> {

    // Metodo para buscar un usuario por su nombre de usuario.
    Optional<User> findByUsername(String username);
}