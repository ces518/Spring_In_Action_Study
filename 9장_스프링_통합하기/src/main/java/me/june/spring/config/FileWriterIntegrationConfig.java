package me.june.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.*;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.router.AbstractMessageRouter;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class FileWriterIntegrationConfig {

    @Bean
    @Transformer(inputChannel = "textInChannel", outputChannel = "fileWriterChannel") // 변환기 선언
    public GenericTransformer<String, String> upperCaseTransformer() {
        return text -> text.toUpperCase();
    }

    @Bean
    @ServiceActivator(inputChannel = "fileWriterChannel") // 파일쓰기 핸들러 선언
    public FileWritingMessageHandler fileWriter() {
        FileWritingMessageHandler handler = new FileWritingMessageHandler(new File("/tmp/sia5/files"));
        handler.setExpectReply(false); // false 로 지정하지 않을경우, 정상 동작하더라도 응답 채널이 구성되지 않았다는 로그가 찍힌다.
        handler.setFileExistsMode(FileExistsMode.APPEND);
        handler.setAppendNewLine(true);
        return handler;
    }

    // Spring Integration DSL 사용
    @Bean
    public IntegrationFlow fileWriterFlow() {
        return IntegrationFlows.from(MessageChannels.direct("textInChannel"))
                .<String, String>transform(t -> t.toUpperCase())
                .channel(MessageChannels.direct("fileWriterChannel")) // 별도 채널을 구성하지 않아도 스프링이 자동 생성해주지만, 필요한 경우 선언할 수 있다.
                .handle(Files
                        .outboundAdapter(new File("/tmp/sia5/files"))
                        .fileExistsMode(FileExistsMode.APPEND)
                        .appendNewLine(true)
                ).get();
    }

    /*
        메시지 채널
     */
    @Bean
    public MessageChannel orderChannel() { // 기본적으로 스프링이 자동생성 해주는 채널은 Direct Channel 이다.
        return new PublishSubscribeChannel();
    }

    // Queue Channel 은 polling 하도록 구성하는것이 중요하다.
    // @ServiceActivator(inputChannel = "someChannel", poller = @Poller(fixedRate = "1000"))
    @Bean
    public MessageChannel someChannel() {
        return new QueueChannel();
    }


    /*
        필터
    */
    @Filter(inputChannel = "numberChannel", outputChannel = "evenNumberChannel")
    public boolean evenNumberFilter(Integer number) {
        return (number & 2) == 0;
    }

    @Bean
    public IntegrationFlow evenNumberFLow(AtomicInteger integerSource) {
        return IntegrationFlows
                .from(MessageChannels.direct("numberChannel"))
                .<Integer>filter(p -> p % 2 == 0)
                .channel(MessageChannels.direct("evenNumberChannel"))
                .get();
    }

    /*
        변환기
        @Bean
        @Transformer(inputChannel = "numberChannel", outputChannel = "romanNumberChannel")
        public GenericTransformer<Integer, String> romanNumTransformer() {
            return RomanNumbers::toRoman;
        }
    */
    @Bean
    @Router(inputChannel = "numberChannel")
    public AbstractMessageRouter evenOddRouter() {
        return new AbstractMessageRouter() {
            @Override
            protected Collection<MessageChannel> determineTargetChannels(Message<?> message) {
                Integer number = (Integer) message.getPayload();
                if (number % 2 == 0) {
                    return Collections.singleton(evenChannel());
                }
                return Collections.singleton(oddChannel());
            }
        };
    }

    @Bean
    public MessageChannel evenChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel oddChannel() {
        return new DirectChannel();
    }
}
