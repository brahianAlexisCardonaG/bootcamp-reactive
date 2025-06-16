package com.bootcamp.project.domain.spi.bootcampmongo;

import com.bootcamp.project.domain.model.bootcampmongo.BootcampMongo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BootcampMongoPersistencePort {
    Mono<Void> saveAll(Flux<BootcampMongo> bootcampMongoFlux);
}
