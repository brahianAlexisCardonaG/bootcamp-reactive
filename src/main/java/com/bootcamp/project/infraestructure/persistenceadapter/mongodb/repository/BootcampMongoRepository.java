package com.bootcamp.project.infraestructure.persistenceadapter.mongodb.repository;

import com.bootcamp.project.infraestructure.persistenceadapter.mongodb.entity.BootcampMongoEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BootcampMongoRepository extends ReactiveMongoRepository<BootcampMongoEntity, Long> {
}
