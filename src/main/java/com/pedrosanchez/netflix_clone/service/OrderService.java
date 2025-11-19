package com.pedrosanchez.netflix_clone.service;

import com.pedrosanchez.netflix_clone.exception.NotFoundException;
import com.pedrosanchez.netflix_clone.model.*;
import com.pedrosanchez.netflix_clone.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final MovieService movieService;

    // Crea una nueva orden a partir del carrito del usuario
    @Transactional
    public Order createOrder(User user, List<Movie> movies) {
        if (movies == null || movies.isEmpty()) {
            throw new IllegalArgumentException("La lista de películas no puede estar vacía");
        }

        try {
            // Cargar las películas completas para evitar problemas de proxy
            List<Movie> managedMovies = new ArrayList<>();
            for (Movie movie : movies) {
                Movie managedMovie = movieService.findById(movie.getId())
                        .orElseThrow(() -> new NotFoundException("Película no encontrada con ID: " + movie.getId()));
                managedMovies.add(managedMovie);
            }

            // Calcular el total (precio fijo de 5.99 por película)
            double total = Math.round(managedMovies.size() * 5.99 * 100.0) / 100.0;

            // Crear y guardar la orden
            Order order = Order.builder()
                    .user(user)
                    .orderDate(LocalDateTime.now())
                    .totalAmount(total)
                    .build();
            
            // Añadir películas a la orden
            managedMovies.forEach(order::addMovie);
            
            Order savedOrder = orderRepository.save(order);
            
            // Vaciar el carrito después de crear la orden
            cartService.clearCart(user);
            
            return savedOrder;
            
        } catch (NotFoundException e) {
            throw e; // Relanzar NotFoundException
        } catch (Exception e) {
            throw new RuntimeException("Error al crear la orden: " + e.getMessage(), e);
        }
    }

    // Obtiene todas las órdenes de un usuario
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }
}
