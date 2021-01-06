package me.june.spring.api.model;

import lombok.Getter;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Date;

// HATEOAS 0.25.x ResourceSupport -> HATEOAS 1.x RepresentationModel 로 변경
@Getter
@Relation(value = "taco", collectionRelation = "tacos") // tacoResourceList 의 Relation 명을 변경한다.
// 이유 ? -> 현재는 자동 생성된 릴레이션명을 사용한다. 자동생성 명은 모델 클래스명 기반. 따라서 클래스명이 변경되면 릴레이션 명도 변경될 소지가 있다.
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