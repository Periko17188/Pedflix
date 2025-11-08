package com.pedrosanchez.netflix_clone.service;

import com.pedrosanchez.netflix_clone.model.Genre;
import com.pedrosanchez.netflix_clone.repository.GenreRepository;
import org.springframework.stereotype.Service;
import java.util.List;

// Servicio que contiene la lógica de negocio relacionada con los géneros de películas
@Service
public class GenreService {

    private final GenreRepository genreRepository;

    // Constructor con inyección de dependencias del repositorio
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    // Obtiene todos los géneros guardados en la base de datos
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    // Guarda un nuevo género o actualiza uno existente
    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }
}