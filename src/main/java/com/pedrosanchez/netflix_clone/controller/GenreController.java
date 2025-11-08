package com.pedrosanchez.netflix_clone.controller;

import com.pedrosanchez.netflix_clone.model.Genre;
import com.pedrosanchez.netflix_clone.service.GenreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// Controlador REST que maneja las operaciones básicas con los géneros de películas
@RestController
@RequestMapping("/api/v1/generos")
public class GenreController {

    private final GenreService genreService;

    // Constructor que recibe el servicio de géneros
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    // Devuelve todos los géneros disponibles
    @GetMapping
    public List<Genre> getAllGenres() {
        return genreService.findAll();
    }

    // Crea un nuevo género y lo guarda en la base de datos
    @PostMapping
    public ResponseEntity<Genre> createGenre(@RequestBody Genre genre) {
        Genre savedGenre = genreService.save(genre);
        return new ResponseEntity<>(savedGenre, HttpStatus.CREATED);
    }
}