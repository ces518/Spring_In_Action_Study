package me.june.spring.receiver.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.june.spring.domain.Order;
import me.june.spring.receiver.OrderReceiver;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;


@Slf4j
@Component
@RequiredArgsConstructor
public class JmsOrderReceiver implements OrderReceiver {
    private final JmsTemplate jmsTemplate;
    private final MessageConverter converter;
    private final Destination orderQueue;

    @Override
    public Order receiveOrder() {
        // 메시지 속성 및 헤더를 살펴볼때 유용하다.
        Message message = jmsTemplate.receive(orderQueue);
        try {
            // 응답 바디만 필요한경우 사용
            jmsTemplate.receiveAndConvert(orderQueue);
            return (Order) converter.fromMessage(message);
        } catch (JMSException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
