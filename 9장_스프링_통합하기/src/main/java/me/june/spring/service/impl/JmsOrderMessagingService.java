package me.june.spring.service.impl;

import lombok.RequiredArgsConstructor;
import me.june.spring.domain.Order;
import me.june.spring.service.OrderMessagingService;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

@Service
@RequiredArgsConstructor
public class JmsOrderMessagingService implements OrderMessagingService {
    private final JmsTemplate jmsTemplate;
    private final Destination orderQueue; // 도착지 큐를 빈으로 등록해두고 사용한다.

    @Override
    public void sendOrder(Order order) {
        // send 메소드를 MessageCreator 를 제공해야 한다.
        jmsTemplate.send(orderQueue, session -> session.createObjectMessage(order));

        // convertAndSend 메소드 사용 MessageCreator 를 사용하지 않아도 된다.
        jmsTemplate.convertAndSend(orderQueue, order);

        // 커스텀 헤더 메시지 추가
        jmsTemplate.send(orderQueue,
                session -> {
                    Message message = session.createObjectMessage(order);
                    message.setStringProperty("X_ORDER_SOURCE", "WEB");
                    return message;
                });

        // convertAndSend 커스텀 헤더 추가
        jmsTemplate.convertAndSend(orderQueue, order, this::addOrderSource);
    }

    private Message addOrderSource(Message message) throws JMSException {
        message.setStringProperty("X_ORDER_SOURCE", "WEB");
        return message;
    }
}
