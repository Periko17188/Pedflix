package com.pedrosanchez.netflix_clone.service;

import com.pedrosanchez.netflix_clone.model.Movie;
import com.pedrosanchez.netflix_clone.repository.MovieRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

// Servicio que contiene la lógica de negocio relacionada con las películas
@Service
public class MovieService {

    private final MovieRepository movieRepository;

    // Constructor con inyección de dependencias del repositorio de películas
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // Obtiene todas las películas de la base de datos
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    // Busca una película por su ID.
    public Optional<Movie> findById(Long id) {
        return movieRepository.findById(id);
    }

    // Guarda una nueva película o actualiza una existente
    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    // Elimina una película por su ID.
    public void deleteById(Long id) {
        movieRepository.deleteById(id);
    }
}