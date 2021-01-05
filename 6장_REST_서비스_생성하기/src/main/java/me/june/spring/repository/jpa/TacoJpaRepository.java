package me.june.spring.repository.jpa;

import me.june.spring.domain.Taco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface TacoJpaRepository extends JpaRepository<Taco, Long> {
}
