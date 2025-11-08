package com.pedrosanchez.netflix_clone.repository;

import com.pedrosanchez.netflix_clone.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repositorio que gestiona las operaciones CRUD para la entidad Genre
@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
}