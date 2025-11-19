package com.pedrosanchez.netflix_clone.repository;

import com.pedrosanchez.netflix_clone.model.Order;
import com.pedrosanchez.netflix_clone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
    // Obtiene todas las órdenes de un usuario
    List<Order> findByUser(User user);

    // Obtiene todas las órdenes de un usuario ordenadas por fecha descendente
    List<Order> findByUserOrderByOrderDateDesc(User user);
}
