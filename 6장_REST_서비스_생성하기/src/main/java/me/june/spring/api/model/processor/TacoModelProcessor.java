package me.june.spring.api.model.processor;

import me.june.spring.api.RecentTacosController;
import me.june.spring.api.model.TacoModel;
import me.june.spring.domain.Taco;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TacoModelProcessor implements RepresentationModelProcessor<CollectionModel<Taco>> {
    // 별도로 생성한 TacoModel 을 사용하면 적용되지 않음.. 이유 ? -> TacoModel 은 우리 직접구현할때만 동작을 한다. Spring-data-REST 는 **Spring-data** 라는 점에 유의 해야한다. Entity 기반으로 동작함!!

    @Override
    public CollectionModel<Taco> process(CollectionModel<Taco> model) {
        return model.add(
                linkTo(methodOn(RecentTacosController.class).recentTacos()).withRel("recent").expand(model)
        );
    }
}
