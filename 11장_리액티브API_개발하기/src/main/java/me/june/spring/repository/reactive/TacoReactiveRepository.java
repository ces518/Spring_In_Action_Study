package me.june.spring.repository.reactive;

import me.june.spring.domain.Taco;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TacoReactiveRepository extends ReactiveCrudRepository<Taco, Long> {
}
