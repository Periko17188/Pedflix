package com.pedrosanchez.netflix_clone.repository;

import com.pedrosanchez.netflix_clone.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repositorio que gestiona las operaciones CRUD para la entidad Movie
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
}
