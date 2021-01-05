package me.june.spring.api.model;

import lombok.Getter;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

// HATEOAS 0.25.x ResourceSupport -> HATEOAS 1.x RepresentationModel 로 변경
@Getter
public class TacoModel extends RepresentationModel<TacoModel> {
    private final String name;
    private final Date createdAt;
    private final CollectionModel<IngredientModel> ingredients;

    public TacoModel(String name, Date createdAt, CollectionModel<IngredientModel> ingredients) {
        this.name = name;
        this.createdAt = createdAt;
        this.ingredients = ingredients;
    }
}
