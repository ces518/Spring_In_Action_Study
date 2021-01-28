package me.june.client.client;

import me.june.client.dto.BookDto;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

@Component
public class BookClient {
    private RestTemplate restTemplate;
    private WebClient.Builder webClientBuilder;

    public BookClient(@LoadBalanced RestTemplate restTemplate, @LoadBalanced WebClient.Builder webClientBuilder) {
        this.restTemplate = restTemplate;
        this.webClientBuilder = webClientBuilder;
    }

    public List<BookDto> findAll() {
        ResponseEntity<List<BookDto>> responseEntity = restTemplate.exchange("http://book-service/books", HttpMethod.GET, null, new ParameterizedTypeReference<List<BookDto>>() {});
        List<BookDto> results = responseEntity.getBody();
        return results;
    }

    public BookDto findById(Long bookId) {
        return restTemplate.getForEntity("http://book-serivce/books/{bookId}", BookDto.class, bookId).getBody();
    }

    public Flux<BookDto> findAllWebClient() {
        return webClientBuilder.build()
                .get()
                .uri("http://book-service/books")
                .retrieve().bodyToFlux(BookDto.class);
    }
}
