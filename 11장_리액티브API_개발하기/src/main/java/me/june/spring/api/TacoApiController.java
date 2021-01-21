package me.june.spring.api;

import lombok.RequiredArgsConstructor;
import me.june.spring.domain.Taco;
import me.june.spring.repository.reactive.TacoReactiveRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tacos")
public class TacoApiController {
    private final TacoReactiveRepository tacoRepository;

    @GetMapping("/recent")
    public Flux<Taco> recent() {
        return tacoRepository.findAll().take(12);
    }

    @GetMapping("/{id}")
    public Mono<Taco> tacoById(@PathVariable Long id) {
        return tacoRepository.findById(id);
    }
}
