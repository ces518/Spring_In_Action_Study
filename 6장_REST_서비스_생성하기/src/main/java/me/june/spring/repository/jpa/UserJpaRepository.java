package me.june.spring.repository.jpa;

import me.june.spring.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserJpaRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
