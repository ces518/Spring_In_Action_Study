package me.june.spring.repository;

import me.june.spring.domain.Taco;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import java.util.UUID;

public interface TacoRepository extends ReactiveCassandraRepository<Taco, UUID> {
}
