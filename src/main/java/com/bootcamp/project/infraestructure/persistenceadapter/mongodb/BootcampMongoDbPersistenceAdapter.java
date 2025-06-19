package com.bootcamp.project.infraestructure.persistenceadapter.mongodb;

import com.bootcamp.project.domain.model.bootcampmongo.BootcampMongo;
import com.bootcamp.project.domain.spi.bootcampmongo.BootcampMongoPersistencePort;
import com.bootcamp.project.infraestructure.persistenceadapter.mongodb.mapper.BootcampMongoEntityMapper;
import com.bootcamp.project.infraestructure.persistenceadapter.mongodb.repository.BootcampMongoRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class BootcampMongoDbPersistenceAdapter implements BootcampMongoPersistencePort {
    private final BootcampMongoRepository repository;
    private final BootcampMongoEntityMapper bootcampMongoEntityMapper;

    @Override
    public Mono<Void> saveAll(List<BootcampMongo> bootcampMongoList) {
        return Flux.fromIterable(bootcampMongoList)
                .map(bootcampMongoEntityMapper::toEntity)
                .collectList()
                .flatMapMany(repository::saveAll)
                .then();
    }

    @Override
    public Mono<List<BootcampMongo>> findByIdBootcamp(List<Long> idBootcamps) {
        return repository.findByIdBootcampIn(idBootcamps)
                .map(bootcampMongoEntityMapper::toModel)
                .collectList();
    }

    @Override
    public Mono<Void> delete(List<Long> idBootcamps) {
        return repository.deleteByIdBootcampIn(idBootcamps)
                .then();
    }
}