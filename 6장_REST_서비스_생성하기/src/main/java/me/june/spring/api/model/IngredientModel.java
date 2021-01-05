package me.june.spring.api.model;

import lombok.Getter;
import me.june.spring.domain.Ingredient;
import org.springframework.hateoas.RepresentationModel;

@Getter
public class IngredientModel extends RepresentationModel<IngredientModel> {
    private final String name;
    private final Ingredient.Type type;

    public IngredientModel(Ingredient ingredient) {
        this.name = ingredient.getName();
        this.type = ingredient.getType();
    }
}
