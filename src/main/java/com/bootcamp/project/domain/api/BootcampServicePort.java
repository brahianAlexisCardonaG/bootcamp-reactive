package com.bootcamp.project.domain.api;

import com.bootcamp.project.domain.model.bootcamp.Bootcamp;
import com.bootcamp.project.domain.model.bootcamp.BootcampListCapabilityTechnology;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BootcampServicePort {
    Mono<List<BootcampListCapabilityTechnology>> saveBootcampCapability(Flux<Bootcamp> bootcampFlux);
    Mono<List<BootcampListCapabilityTechnology>> findCapabilitiesByIdBootcamps(List<Long> capabilityId,
                                                                               String order,
                                                                               int skip,
                                                                               int rows);
    Mono<Void> deleteBootcamps(List<Long> bootcampIds);

    Flux<Bootcamp> getBootcampsByIds(List<Long> bootcampIds);
}
