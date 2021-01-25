package me.june.spring.api;

import lombok.RequiredArgsConstructor;
import me.june.spring.domain.Taco;
import me.june.spring.repository.TacoRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tacos")
public class TacoController {
    private final TacoRepository tacoRepository;

    @GetMapping
    public Flux<Taco> getTacos() {
        return tacoRepository.findAll().take(12);
    }
}
