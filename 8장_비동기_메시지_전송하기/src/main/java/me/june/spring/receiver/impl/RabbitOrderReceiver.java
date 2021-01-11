package me.june.spring.receiver.impl;

import lombok.RequiredArgsConstructor;
import me.june.spring.domain.Order;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitOrderReceiver {
    private final RabbitTemplate rabbitTemplate;
    private final MessageConverter messageConverter;

    // 메시지 수신은 Jms와 동일하게 풀, 푸시 방식이 존재함
    // rabbitTemplate 을 사용하는것은 pull 방식
    public Order receiveOrder() {
        // rabbitTemplate 을 사용한 수신
        Message message = rabbitTemplate.receive("tacocloud.order", 30000); // 타임아웃 지정도 가능하다.
        Order order = message != null ? (Order) messageConverter.fromMessage(message) : null;

        // receiveAndConvert 를 사용
        return rabbitTemplate.receiveAndConvert("tacocloud.order", new ParameterizedTypeReference<>(){});
    }
}
