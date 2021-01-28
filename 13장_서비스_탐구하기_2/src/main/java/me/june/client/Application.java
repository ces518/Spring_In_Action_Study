package me.june.client;

import me.june.client.entity.Book;
import me.june.client.repository.BookRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ApplicationRunner init(BookRepository bookRepository) {
        return args -> {
            bookRepository.save(new Book("제목A", "저자A", "A 책입니다."));
            bookRepository.save(new Book("제목B", "저자B", "B 책입니다."));
            bookRepository.save(new Book("제목C", "저자C", "C 책입니다."));
        };
    }
}