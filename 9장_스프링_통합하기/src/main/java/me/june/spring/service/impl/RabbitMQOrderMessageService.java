package me.june.spring.service.impl;

import lombok.RequiredArgsConstructor;
import me.june.spring.domain.Order;
import me.june.spring.service.OrderMessagingService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQOrderMessageService implements OrderMessagingService {
    private final RabbitTemplate rabbitTemplate; // EvaluationContext 를 내부적으로 가지고있다. -> SpEL 을 지원함..
    // https://docs.spring.io/spring-amqp/docs/current/reference/html/
    // SpEL 관련글 https://jaehun2841.github.io/2018/11/21/2018-11-21-spel-expression/#spel에서-지원하는-기능

    // RabbitListenerBeanPostProcessor 에서는 BeanExpressionResolver 를 사용중... -> StandardBeanExpressionResolver 를 사용하고... 내부적으로 SpelExpressionParser 를 사용한다.
    // EvaluationContext 와 차이점은 무엇일까 ? -> EvaluationContext 는 생성비용은 크지만 SpEL 을 캐싱한다는 점...
    // https://docs.spring.io/spring-framework/docs/3.0.0.M4/reference/html/ch06s03.html
    // https://docs.spring.io/spring-framework/docs/4.3.10.RELEASE/spring-framework-reference/html/expressions.html
    // 킹웃사이더님 의 레퍼런스 번역 -> https://blog.outsider.ne.kr/835

    @Override
    public void sendOrder(Order order) {
        // RabbitMQ 도 JMS 와 동일하게 MessageConverter 를 사용해서 변환을 시도한다.
        // 기본 적으로 SimpleMessageConverter 를 사용하며, Jackson2JsonMessageConverter 등을 제공한다.
        MessageConverter converter = rabbitTemplate.getMessageConverter();
        MessageProperties props = new MessageProperties();
        // Header 설정도 가능하다.
        props.setHeader("X_ORDER_SOURCE", "WEB");
        Message message = converter.toMessage(order, props);
        rabbitTemplate.send("tacocloud.order", message);
        rabbitTemplate.convertAndSend("tacocloud.order", order);

        // convertAndSend 사용시 header 설정은 MessagePostProcessor 에서 해야한다.
        rabbitTemplate.convertAndSend("tacocloud.order.queue", order, message1 -> {
            message1.getMessageProperties().setHeader("X_ORDER_SOURCE", "WEB");
            return message1;
        });
    }
}
