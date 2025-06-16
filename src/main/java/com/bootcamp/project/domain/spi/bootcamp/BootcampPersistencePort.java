package com.bootcamp.project.domain.spi.bootcamp;

import com.bootcamp.project.domain.model.bootcamp.Bootcamp;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BootcampPersistencePort {
    Flux<Bootcamp> save(Flux<Bootcamp> bootcampFlux);
    Mono<Boolean> findByName(String name);
    Mono<List<Bootcamp>> findByAllIds(List<Long> ids);
}
