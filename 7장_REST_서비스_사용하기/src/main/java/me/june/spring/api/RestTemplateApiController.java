package me.june.spring.api;

import lombok.RequiredArgsConstructor;
import me.june.spring.domain.Ingredient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/rest")
@RequiredArgsConstructor
public class RestTemplateApiController {
    private final RestTemplate restTemplate; // RestTemplate 의 기본 구현체 ? -> JDK 의 기본 구현체 HttpUrlConnection (SimpleBufferingClientHttpRequest).. 구현체를 변경할수 있을까 ? 커넥션풀 설정은 ?
    // OkHttp 를 기본으로 지원한다. 4.3버전 부터...
    // HttpMessageConverter 를 사용한다...

    @GetMapping("/ingredient/{id}")
    public Ingredient getIngredientById(@PathVariable String id) {
        Ingredient ingredient = restTemplate.getForObject("http://localhost:8080/api/ingredients/{id}", Ingredient.class, id);

        // URI 타입 파라미터를 사용할 경우 URI 객체를 구성해서 사용해야 한다.
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/api/ingredients/{id}")
                .build(Map.of("id", id));
        restTemplate.getForObject(uri, Ingredient.class);

        // getForEntity 는 ResponseEntity 타입을 반환한다. 응답 헤더와 같은 상셍한 내용 조회 가능
        ResponseEntity<Ingredient> responseEntity = restTemplate.getForEntity(uri, Ingredient.class);
        return ingredient;
    }

    @PostMapping("/ingredient")
    public Ingredient createIngredient(@RequestBody Ingredient ingredient) {
        return restTemplate.postForObject("http://localhost:8080/api/ingredients", ingredient, Ingredient.class);
    }

    @PutMapping("/ingredient/{id}")
    public void updateIngredient(@PathVariable String id, @RequestBody Ingredient ingredient) {
        restTemplate.put("http://localhost:8080/api/ingredients/{id}", ingredient, id);
    }

    @DeleteMapping("/ingredient/{id}")
    public void deleteIngredient(@PathVariable String id) {
        restTemplate.delete("http://localhost:8080/api/ingredients/{id}", id);
    }
}
