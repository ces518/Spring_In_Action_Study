package me.june.spring.api;

import lombok.RequiredArgsConstructor;
import me.june.spring.domain.Ingredient;
import me.june.spring.repository.IngredientRepository;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
public class IngredientController {
    private final IngredientRepository ingredientRepository;

    @GetMapping
    public Flux<Ingredient> findAllIngredients() {
        return ingredientRepository.findAll();
    }

    @PostMapping
    public Mono<Ingredient> saveIngredient(@RequestBody Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }
}
