package me.june.spring.repository;

import me.june.spring.domain.Book;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface BookRepository extends ReactiveCrudRepository<Book, String> {

    Flux<Book> findByOrderByAuthorDesc();
}
