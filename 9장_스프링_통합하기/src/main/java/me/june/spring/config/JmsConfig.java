package me.june.spring.config;

import me.june.spring.domain.Order;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;

import javax.jms.Destination;
import java.util.Map;

@Configuration
public class JmsConfig {

    @Bean
    public Destination orderQueue() {
        return new ActiveMQQueue("tacocloud.order.queue");
    }

    // Jms 의 기본 전략은 SimpleMessageConverter 를 사용한다.
    // 하지만 이는 Serializable 을 구현하기 때문에 사용하기 불편하다는 단점이 있음.
    // MappingJackson2MessageConverter 을 빈으로 등록할 경우 자동 설정에 의해 MappingJackson2MessageConverter 를 사용한다.
    // 주의할점은 Spring 의 HttpMessageConverter 와 는 다른 인터페이스 라는점... 혼동해선 안된다.
    @Bean
    public MappingJackson2MessageConverter messageConverter() {
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setTypeIdPropertyName("_typeId");

        Map<String, Class<?>> typeIdMappings = Map.of("order", Order.class);
        messageConverter.setTypeIdMappings(typeIdMappings);
        return messageConverter;
    }
}
