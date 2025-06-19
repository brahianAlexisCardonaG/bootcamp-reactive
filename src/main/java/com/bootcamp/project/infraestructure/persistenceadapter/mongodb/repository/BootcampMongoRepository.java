package com.bootcamp.project.infraestructure.persistenceadapter.mongodb.repository;

import com.bootcamp.project.domain.model.bootcampmongo.BootcampMongo;
import com.bootcamp.project.infraestructure.persistenceadapter.mongodb.entity.BootcampMongoEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BootcampMongoRepository extends ReactiveMongoRepository<BootcampMongoEntity, Long> {
    Flux<BootcampMongoEntity> findByIdBootcampIn(List<Long> idBootcamps);
    Mono<Void> deleteByIdBootcampIn(List<Long> idBootcamps);
}
