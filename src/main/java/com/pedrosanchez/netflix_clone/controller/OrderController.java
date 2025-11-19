package com.pedrosanchez.netflix_clone.controller;

import com.pedrosanchez.netflix_clone.exception.NotFoundException;
import com.pedrosanchez.netflix_clone.model.*;
import com.pedrosanchez.netflix_clone.repository.UserRepository;
import com.pedrosanchez.netflix_clone.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CartService cartService;
    private final OrderService orderService;
    private final UserRepository userRepository;

    // Finaliza la compra del carrito
    @PostMapping("/checkout")
    @Transactional
    public ResponseEntity<?> checkout(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

            List<Movie> movies = cartService.getCartItems(user)
                    .stream()
                    .map(CartItem::getMovie)
                    .collect(Collectors.toList());

            if (movies.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "El carrito está vacío");
                return ResponseEntity.badRequest().body(response);
            }

            Order order = orderService.createOrder(user, movies);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Compra realizada con éxito");
            response.put("orderId", order.getId());
            response.put("totalItems", movies.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al procesar la compra: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Muestra todas las compras del usuario
    @GetMapping
    public ResponseEntity<?> getOrders(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

            return ResponseEntity.ok(orderService.getOrdersByUser(user));
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al obtener las órdenes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}