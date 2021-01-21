package me.june.spring.config;

import lombok.RequiredArgsConstructor;
import me.june.spring.domain.Taco;
import me.june.spring.repository.reactive.TacoReactiveRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.data.repository.core.support.RepositoryComposition.RepositoryFragments.just;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
@RequiredArgsConstructor
public class RouterFunctionConfig {
    private final TacoReactiveRepository tacoRepository;

    // 리액티브 API 를 정의하기 위한 함수형 프로그래밍 모델
    // 4가지 필수요소가 있다.
    // RequestPredicate : 요청의 종류 선언
    // RouterFunction : 일치하는 요청이 핸들러에 어떻게 전달되어야 하는지 선언
    // ServerRequest : HTTP 요청 / 헤더와 몸체정보 참조 가능
    // ServerResponse : HTTP 응답 / 헤더와 몸체정보 포함
    @Bean
    public RouterFunction<?> helloRouterFunction() {
            // GET hello 요청을 받는 Router 를 선언한다.
        return route(GET("/hello"), request -> ok().body(just("Hello World!"), String.class))
            // andRoute 를 통해 또다른 핸들러를 추가할 수 있다.
            .andRoute(GET("/bye"), request -> ok().body(just("See ya!"), String.class));
    }

    @Bean
    public RouterFunction<?> routerFunction() {
        return route(GET("/design/taco"), this::recents);
    }

    public Mono<ServerResponse> recents(ServerRequest request) {
        return ServerResponse.ok()
                .body(tacoRepository.findAll().take(12), Taco.class);
    }
}
