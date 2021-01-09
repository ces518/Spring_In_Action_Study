package me.june.spring.service;

import me.june.spring.domain.Order;

public interface OrderMessagingService {
    void sendOrder(Order order);
}
