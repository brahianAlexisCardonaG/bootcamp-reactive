package com.bootcamp.project.domain.api;

import com.bootcamp.project.domain.model.personbootcamp.PersonListBootcampCapTech;
import reactor.core.publisher.Mono;

public interface PersonBootcampServicePort {
    Mono<PersonListBootcampCapTech> getPersonsByBootcampsByIdMaxNumberPerson();
}
