package me.june.spring.repository;

import me.june.spring.domain.User;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepository extends ReactiveCassandraRepository<User, UUID> {

    // 카산드라는 where 절을 허용하지 않는다, allowfiltering 옵션을 주어 where 절을 허용해야한다.
    @AllowFiltering
    Mono<User> findByUsername(String username);
}
