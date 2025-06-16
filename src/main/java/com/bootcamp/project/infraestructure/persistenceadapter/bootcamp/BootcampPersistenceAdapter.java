package com.bootcamp.project.infraestructure.persistenceadapter.bootcamp;

import com.bootcamp.project.domain.model.bootcamp.Bootcamp;
import com.bootcamp.project.domain.spi.bootcamp.BootcampPersistencePort;
import com.bootcamp.project.infraestructure.persistenceadapter.bootcamp.mapper.BootcampEntityMapper;
import com.bootcamp.project.infraestructure.persistenceadapter.bootcamp.repository.BootcampRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class BootcampPersistenceAdapter implements BootcampPersistencePort {

    private final BootcampEntityMapper bootcampEntityMapper;
    private final BootcampRepository bootcampRepository;

    @Override
    public Flux<Bootcamp> save(Flux<Bootcamp> bootcampFlux) {
        return bootcampFlux
                .map(bootcampEntityMapper::toEntity)
                .collectList()
                .flatMapMany(bootcampRepository::saveAll)
                .map(bootcampEntityMapper::toModel);
    }

    @Override
    public Mono<Boolean> findByName(String name) {
        return bootcampRepository.findByName(name)
                .map(bootcampEntityMapper::toModel)
                .map(tech -> true)
                .defaultIfEmpty(false);
    }

    @Override
    public Mono<List<Bootcamp>> findByAllIds(List<Long> ids) {
        return Flux.fromIterable(ids)
                .collectList() // opcional, si ids ya viene como lista
                .flatMapMany(bootcampRepository::findAllById)// devuelve Flux<Capability>
                .map(bootcampEntityMapper::toModel)
                .collectList();
    }
}
