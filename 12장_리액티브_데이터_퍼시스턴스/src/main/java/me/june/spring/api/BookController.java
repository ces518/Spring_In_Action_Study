package me.june.spring.api;

import lombok.RequiredArgsConstructor;
import me.june.spring.domain.Book;
import me.june.spring.repository.BookRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {
    private final BookRepository bookRepository;

    @GetMapping
    public Flux<Book> getBooks() {
        return bookRepository.findAll();
    }
}
