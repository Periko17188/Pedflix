package com.pedrosanchez.netflix_clone.service;

import com.pedrosanchez.netflix_clone.model.Movie;
import com.pedrosanchez.netflix_clone.repository.MovieRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

// Componente de servicio para la lógica de negocio relacionada con las películas.
@Service
public class MovieService {

    private final MovieRepository movieRepository;

    // Inyección de dependencia del repositorio de películas.
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // Devuelve todas las películas.
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    // Busca una película por su ID.
    public Optional<Movie> findById(Long id) {
        return movieRepository.findById(id);
    }

    // Guarda o actualiza una película.
    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    // Elimina una película por su ID.
    public void deleteById(Long id) {
        movieRepository.deleteById(id);
    }
}