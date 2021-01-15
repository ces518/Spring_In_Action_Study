package me.june.spring.listener;

import lombok.extern.slf4j.Slf4j;
import me.june.spring.domain.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderKafkaListener {

    @KafkaListener(topics = "tacocloud.orders.topic")
    public void handle(Order order, ConsumerRecord<String, Order> record, Message<Order> message) {
        // ConsumerRecord 는 수신된 메시지의 파티션, 타임 스탬프를 받을 수 있다.
        log.info("partition = {}, timestamp = {}", record.partition(), record.timestamp());

        // Message 객체로 받으면 같은 일을 처리할 수 있다.
        MessageHeaders headers = message.getHeaders();
        log.info("partition = {}, timestamp = {}", headers.get(KafkaHeaders.RECEIVED_PARTITION_ID), headers.get(KafkaHeaders.RECEIVED_TIMESTAMP));

        // doSomething
    }
}
