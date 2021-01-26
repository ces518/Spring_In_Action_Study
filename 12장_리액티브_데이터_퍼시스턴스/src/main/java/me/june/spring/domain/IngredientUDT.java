package me.june.spring.domain;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@UserDefinedType("ingredient")
public class IngredientUDT {

    private String name;
    private Ingredient.Type type;
}
