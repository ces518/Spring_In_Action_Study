package me.june.client.api;

import me.june.client.client.BookClient;
import me.june.client.client.BookFeignClient;
import me.june.client.dto.BookDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.ws.rs.Path;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final BookClient bookClient;
    private final BookFeignClient bookFeignClient;

    public OrderController(BookClient bookClient, BookFeignClient bookFeignClient) {
        this.bookClient = bookClient;
        this.bookFeignClient = bookFeignClient;
    }

    @GetMapping("/books")
    public List<BookDto> getBooks() {
        return bookClient.findAll();
    }

    @GetMapping("/books/{id}")
    public BookDto getBook(@PathVariable Long id) {
        return bookClient.findById(id);
    }

    @GetMapping("/books/flux")
    public Flux<BookDto> getBookFlux() {
        return bookClient.findAllWebClient();
    }

    @GetMapping("/books/feign")
    public List<BookDto> getBooksFeign() {
        return bookFeignClient.getBooks();
    }

    @GetMapping("/books/{id}/feign")
    public BookDto getBookFeign(@PathVariable Long id) {
        return bookFeignClient.getBook(id);
    }
}
