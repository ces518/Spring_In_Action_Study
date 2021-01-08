package me.june.spring.api;

import me.june.spring.domain.Ingredient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.hateoas.server.core.TypeReferences;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/api/traverson")
public class TraversonApiController {
    private final Traverson traverson = new Traverson(
            URI.create("http://localhost:8080/api"), MediaTypes.HAL_JSON
    );

    // https://github.com/spring-projects/spring-hateoas-examples/blob/master/api-evolution/new-client/src/main/java/org/springframework/hateoas/examples/HomeController.java
    @GetMapping("/ingredients")
    public Collection<Ingredient> ingredients() {

        // spring.core
        ParameterizedTypeReference<CollectionModel<Ingredient>> ingredientType = new ParameterizedTypeReference<>() {};

        // spring.hateoas
        TypeReferences.CollectionModelType<EntityModel<Ingredient>> ingredientType2 = new TypeReferences.CollectionModelType<>() {};
        CollectionModel<Ingredient> ingredients = traverson.follow("ingredients")
                .toObject(ingredientType);
        Collection<Ingredient> content = ingredients.getContent();

        return content;
    }
}
