package me.june.spring.api;

import lombok.RequiredArgsConstructor;
import me.june.spring.domain.Taco;
import me.june.spring.repository.TacoRepository;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tacos")
public class TacoController {
    private final TacoRepository tacoRepository;

    @GetMapping
    public Flux<Taco> getTacos() {
        return tacoRepository.findAll().take(12);
    }

    @PostMapping
    public Mono<Taco> saveTaco(@RequestBody Taco taco) {
        return tacoRepository.save(taco);
    }
}
