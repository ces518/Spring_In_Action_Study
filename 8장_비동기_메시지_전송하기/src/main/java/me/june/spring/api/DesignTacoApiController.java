package me.june.spring.api;

import lombok.RequiredArgsConstructor;
import me.june.spring.api.assembler.TacoModelAssembler;
import me.june.spring.api.model.TacoModel;
import me.june.spring.domain.Taco;
import me.june.spring.repository.jpa.TacoJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/design", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_XML_VALUE })
@CrossOrigin(origins = "*") // CrossOrigin 은 무엇인가 ?
@RequiredArgsConstructor
public class DesignTacoApiController {
    private final TacoJpaRepository tacoRepository;
    private final TacoModelAssembler tacoModelAssembler;

    @GetMapping("/recent")
    public CollectionModel<TacoModel> recentTacos() {
        PageRequest page = PageRequest.of(0, 12, Sort.by("createdAt").descending());
        // Spring-HATEOAS 0.25.x 기준 Resource -> 1.x 로 올라오면서 EntityModel, CollectionModel 로 변경됨
        List<Taco> tacos = tacoRepository.findAll(page).getContent();
//        return CollectionModel.of(tacos, linkTo(methodOn(DesignTacoApiController.class).recentTacos()).withRel("recents"));
        return tacoModelAssembler.toCollectionModel(tacos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Taco> tacoById(@PathVariable("id") Long id) { // @PathVariable 은 어떻게 동작하는가 ? -> PathVariableMethodArgumentResolver
        Taco taco = tacoRepository.findById(id).orElse(null); // Optional 에 관한 고찰. 코.다.기 내용 참조
        if (taco != null) {
            return ResponseEntity.ok(taco);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE) // -> consumes 가 의미하는것은 무엇일까 ?
    @ResponseStatus(HttpStatus.CREATED)
    public Taco postTaco(@RequestBody Taco taco) { // @RequestBody 는 어떻게 동작하는가 ?
        return tacoRepository.save(taco);
    }
}
