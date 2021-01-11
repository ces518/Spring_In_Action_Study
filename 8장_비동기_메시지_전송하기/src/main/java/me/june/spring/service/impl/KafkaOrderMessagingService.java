package me.june.spring.service.impl;

import lombok.RequiredArgsConstructor;
import me.june.spring.domain.Order;
import me.june.spring.service.OrderMessagingService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaOrderMessagingService implements OrderMessagingService {
    private final KafkaTemplate<String, Order> kafkaTemplate;

    @Override
    public void sendOrder(Order order) {
        kafkaTemplate.send("tacocloud.orders.topic", order);
    }
}
