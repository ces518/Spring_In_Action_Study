package me.june.spring.api;

import lombok.RequiredArgsConstructor;
import me.june.spring.api.assembler.TacoModelAssembler;
import me.june.spring.api.model.TacoModel;
import me.june.spring.domain.Taco;
import me.june.spring.repository.jpa.TacoJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RepositoryRestController // Spring-data-REST 의 엔드포인트를 추가하기 위한 구현체 등록
@RequiredArgsConstructor
public class RecentTacosController {
    private final TacoJpaRepository tacoRepository;
    private final TacoModelAssembler tacoModelAssembler;

    @GetMapping(path = "/api/tacos/recent", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity recentTacos() {
        Pageable page = PageRequest.of(0, 12, Sort.by("createdAt").descending());
        List<Taco> tacos = tacoRepository.findAll(page).getContent();
        return ResponseEntity.ok(tacoModelAssembler.toCollectionModel(tacos));
    }
}
