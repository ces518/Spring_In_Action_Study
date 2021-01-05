package me.june.spring.repository;

import me.june.spring.domain.Order;
import me.june.spring.domain.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderRepository {
    Order save(Order order);
}
