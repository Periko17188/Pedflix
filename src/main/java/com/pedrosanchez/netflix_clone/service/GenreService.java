package com.pedrosanchez.netflix_clone.service;

import com.pedrosanchez.netflix_clone.model.Genre;
import com.pedrosanchez.netflix_clone.repository.GenreRepository;
import org.springframework.stereotype.Service;
import java.util.List;

// Componente de servicio para la lógica de negocio relacionada con los géneros.
@Service
public class GenreService {

    private final GenreRepository genreRepository;

    // Inyección de dependencia del repositorio de géneros.
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    // Devuelve todos los géneros de la base de datos.
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    // Guarda o actualiza un género.
    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }
}