package com.bootcamp.project.infraestructure.persistenceadapter.mongodb;

import com.bootcamp.project.domain.model.bootcampmongo.BootcampMongo;
import com.bootcamp.project.domain.spi.bootcampmongo.BootcampMongoPersistencePort;
import com.bootcamp.project.infraestructure.persistenceadapter.mongodb.mapper.BootcampMongoEntityMapper;
import com.bootcamp.project.infraestructure.persistenceadapter.mongodb.repository.BootcampMongoRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BootcampMongoDbPersistenceAdapter implements BootcampMongoPersistencePort {
    private final BootcampMongoRepository repository;
    private final BootcampMongoEntityMapper bootcampMongoEntityMapper;

    @Override
    public Mono<Void> saveAll(Flux<BootcampMongo> bootcampMongoFlux) {
        return bootcampMongoFlux
                .map(bootcampMongoEntityMapper::toEntity)
                .collectList()
                .flatMapMany(repository::saveAll)
                .then();
    }
}