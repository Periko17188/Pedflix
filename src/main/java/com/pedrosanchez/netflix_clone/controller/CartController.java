package com.pedrosanchez.netflix_clone.controller;

import com.pedrosanchez.netflix_clone.model.CartItem;
import com.pedrosanchez.netflix_clone.model.Movie;
import com.pedrosanchez.netflix_clone.model.User;
import com.pedrosanchez.netflix_clone.repository.MovieRepository;
import com.pedrosanchez.netflix_clone.repository.UserRepository;
import com.pedrosanchez.netflix_clone.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public CartController(CartService cartService, UserRepository userRepository, MovieRepository movieRepository) {
        this.cartService = cartService;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    // Añade una película al carrito
    @PostMapping("/add/{movieId}")
    public ResponseEntity<?> addToCart(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long movieId) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        Movie movie = movieRepository.findById(movieId).orElseThrow();
        cartService.addToCart(user, movie);
        return ResponseEntity.ok("Película añadida al carrito");
    }

    // Muestra el contenido del carrito
    @GetMapping
    public List<CartItem> viewCart(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        return cartService.getCartItems(user);
    }

    // Elimina un artículo del carrito
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeItem(@PathVariable Long id) {
        cartService.removeFromCart(id);
        return ResponseEntity.ok("Artículo eliminado del carrito");
    }
}
