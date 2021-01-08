package me.june.spring.repository.jpa;

import me.june.spring.domain.Taco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TacoJpaRepository extends JpaRepository<Taco, Long> {
}
