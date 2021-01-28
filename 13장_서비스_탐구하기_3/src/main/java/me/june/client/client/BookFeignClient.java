package me.june.client.client;

import me.june.client.dto.BookDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("book-service")
public interface BookFeignClient {

    @GetMapping("/books")
    List<BookDto> getBooks();

    @GetMapping("/books/{id}")
    BookDto getBook(@PathVariable Long id);
}
