package me.june.spring.api;

import lombok.RequiredArgsConstructor;
import me.june.spring.domain.Ingredient;
import me.june.spring.repository.IngredientRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
public class IngredientController {
    private final IngredientRepository ingredientRepository;

    @GetMapping
    public Flux<Ingredient> findAllIngredients() {
        return ingredientRepository.findAll();
    }
}
