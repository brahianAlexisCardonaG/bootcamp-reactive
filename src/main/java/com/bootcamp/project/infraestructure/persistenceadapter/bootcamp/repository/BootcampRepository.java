package com.bootcamp.project.infraestructure.persistenceadapter.bootcamp.repository;

import com.bootcamp.project.infraestructure.persistenceadapter.bootcamp.entity.BootcampEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface BootcampRepository extends ReactiveCrudRepository<BootcampEntity, Long> {
    Mono<BootcampEntity> findByName(String name);
}
