package com.pedrosanchez.netflix_clone.controller;

import com.pedrosanchez.netflix_clone.model.Genre;
import com.pedrosanchez.netflix_clone.model.Movie;
import com.pedrosanchez.netflix_clone.repository.GenreRepository;
import com.pedrosanchez.netflix_clone.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// Controlador REST para gestionar operaciones con películas (CRUD)
@RestController
@RequestMapping("/api/v1/peliculas")
public class MovieController {

    private final MovieService movieService;
    private final GenreRepository genreRepository;

    // Inyección de dependencias
    public MovieController(MovieService movieService, GenreRepository genreRepository) {
        this.movieService = movieService;
        this.genreRepository = genreRepository;
    }

    // Obtiene todas las películas
    @GetMapping
    public List<Movie> getAllMovies() {
        try {
            return movieService.findAll();
        } catch (Exception e) {
            System.err.println("Error al obtener todas las películas: " + e.getMessage());
            return List.of();
        }
    }

    // Obtiene una película por ID
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        return movieService.findById(id)
                .map(movie -> new ResponseEntity<>(movie, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Crea una nueva película a partir de los datos recibidos desde el frontend.
    @PostMapping
    public ResponseEntity<?> createMovie(@RequestBody Map<String, Object> payload) {
        try {
            Movie movie = new Movie();
            // Asignar campos de la película
            if (payload.get("titulo") instanceof String) movie.setTitulo((String) payload.get("titulo"));
            if (payload.get("sinopsis") instanceof String) movie.setSinopsis((String) payload.get("sinopsis"));
            if (payload.get("anio") instanceof Number) movie.setAnio(((Number) payload.get("anio")).intValue());
            if (payload.get("imagenUrl") instanceof String) movie.setImagenUrl((String) payload.get("imagenUrl"));
            if (payload.get("rating") instanceof Number) movie.setRating(((Number) payload.get("rating")).doubleValue());

            // Procesamiento de IDs de género
            Object genreIdsObj = payload.get("genreIds");
            List<Long> genreIds = new ArrayList<>();

            if (genreIdsObj instanceof List<?>) {
                List<?> rawList = (List<?>) genreIdsObj;
                genreIds = rawList.stream()
                        .filter(obj -> obj instanceof Number)
                        .map(obj -> ((Number) obj).longValue())
                        .collect(Collectors.toList());
            }

            if (!genreIds.isEmpty()) {
                Set<Genre> genres = new HashSet<>(genreRepository.findAllById(genreIds));
                movie.setGeneros(genres);
            } else {
                movie.setGeneros(new HashSet<>());
            }

            // Guarda la película en la base de datos
            Movie savedMovie = movieService.save(movie);
            return new ResponseEntity<>(savedMovie, HttpStatus.CREATED);
        } catch (ClassCastException cce) {
            System.err.println("Error de tipo al procesar payload de creación: " + cce.getMessage());
            return new ResponseEntity<>("Datos inválidos en la petición.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.err.println("Error al crear película: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>("Error interno del servidor.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualiza los datos de una película existente según su ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        return movieService.findById(id)
                .map(existingMovie -> {
                    try {
                        // Actualizar campos de la película
                        if (payload.containsKey("titulo") && payload.get("titulo") instanceof String) existingMovie.setTitulo((String) payload.get("titulo"));
                        if (payload.containsKey("sinopsis") && payload.get("sinopsis") instanceof String) existingMovie.setSinopsis((String) payload.get("sinopsis"));
                        if (payload.containsKey("anio") && payload.get("anio") instanceof Number) existingMovie.setAnio(((Number) payload.get("anio")).intValue());
                        if (payload.containsKey("imagenUrl")) existingMovie.setImagenUrl((String) payload.get("imagenUrl"));
                        if (payload.containsKey("rating")) {
                            Object ratingObj = payload.get("rating");
                            if (ratingObj instanceof Number) {
                                existingMovie.setRating(((Number) ratingObj).doubleValue());
                            } else if (ratingObj == null) {
                                existingMovie.setRating(null);
                            } else {
                                try {
                                    existingMovie.setRating(Double.parseDouble(ratingObj.toString()));
                                } catch (NumberFormatException nfe) {
                                    System.err.println("Rating inválido recibido en actualización: " + ratingObj);
                                }
                            }
                        }

                        // Actualiza los géneros si se reciben nuevos IDs.
                        if (payload.containsKey("genreIds")) {
                            Object genreIdsObj = payload.get("genreIds");
                            List<Long> genreIds = new ArrayList<>();

                            if (genreIdsObj instanceof List<?>) {
                                List<?> rawList = (List<?>) genreIdsObj;
                                genreIds = rawList.stream()
                                        .filter(obj -> obj instanceof Number)
                                        .map(obj -> ((Number) obj).longValue())
                                        .collect(Collectors.toList());

                                Set<Genre> genres = new HashSet<>(genreRepository.findAllById(genreIds));
                                existingMovie.setGeneros(genres);

                            } else {
                                System.err.println("Campo 'genreIds' recibido pero no es una lista válida: " + genreIdsObj);
                                existingMovie.setGeneros(new HashSet<>());
                            }
                        }

                        // Guarda los cambios en la base de datos
                        Movie updatedMovie = movieService.save(existingMovie);
                        return new ResponseEntity<>(updatedMovie, HttpStatus.OK);

                    } catch (ClassCastException cce) {
                        System.err.println("Error al procesar datos para ID " + id + ": " + cce.getMessage());
                        return new ResponseEntity<>("Datos inválidos en la petición.", HttpStatus.BAD_REQUEST);
                    } catch (Exception e) {
                        System.err.println("Error al actualizar película con ID " + id + ": " + e.getMessage());
                        e.printStackTrace();
                        return new ResponseEntity<>("Error interno del servidor.", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Elimina una película por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        try {
            movieService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            System.err.println("Error al eliminar película con ID " + id + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

