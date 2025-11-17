package com.pedrosanchez.netflix_clone.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_item")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usuario al que pertenece el carrito
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Película añadida al carrito
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    // Precio simulado de la película
    private Double price;

    public CartItem() {}

    public CartItem(User user, Movie movie, Double price) {
        this.user = user;
        this.movie = movie;
        this.price = price;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Movie getMovie() { return movie; }
    public void setMovie(Movie movie) { this.movie = movie; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
