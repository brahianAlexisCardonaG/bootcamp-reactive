package com.bootcamp.project.domain.spi.bootcampmongo;

import com.bootcamp.project.domain.model.bootcampmongo.BootcampMongo;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BootcampMongoPersistencePort {
    Mono<Void> saveAll(List<BootcampMongo> bootcampMongoFlux);
    Mono<List<BootcampMongo>> findByIdBootcamp(List<Long> idBootcamps);
    Mono<Void> delete(List<Long> idBootcamps);
}
