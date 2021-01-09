package me.june.spring.api.assembler;

import me.june.spring.api.DesignTacoApiController;
import me.june.spring.api.model.TacoModel;
import me.june.spring.domain.Taco;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;


// SimpleRepresentationModelAssembler 인터페이스도 제공중이다..
@Component
public class TacoModelAssembler extends RepresentationModelAssemblerSupport<Taco, TacoModel> {
    private final IngredientModelAssembler ingredientModelAssembler;

    public TacoModelAssembler(IngredientModelAssembler ingredientModelAssembler) {
        super(DesignTacoApiController.class, TacoModel.class);
        this.ingredientModelAssembler = ingredientModelAssembler;
    }

    @Override
    protected TacoModel instantiateModel(Taco entity) {
        return new TacoModel(entity.getName(), entity.getCreatedAt(), ingredientModelAssembler.toCollectionModel(entity.getIngredients()));
    }

    @Override
    public TacoModel toModel(Taco entity) {
        return createModelWithId(entity.getId(), entity);
    }
}
