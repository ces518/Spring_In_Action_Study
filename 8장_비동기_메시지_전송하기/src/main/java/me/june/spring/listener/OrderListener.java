package me.june.spring.listener;

import me.june.spring.domain.Order;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class OrderListener {

    // 특정 요청에 대한 Listener 등록
    @JmsListener(destination = "tacocloud.order.queue")
    public void receiveOrder(Order order) {
        // doSomething
    }
}
