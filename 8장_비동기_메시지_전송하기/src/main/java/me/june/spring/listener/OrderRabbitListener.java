package me.june.spring.listener;

import me.june.spring.domain.Order;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderRabbitListener {

    // @JmsListener 와 거의 동일하게 동작한다.
    @RabbitListener(queues = "tacocloud.order.queue")
    public void receiveOrder(Order order) {
        // doSomething
    }
}
