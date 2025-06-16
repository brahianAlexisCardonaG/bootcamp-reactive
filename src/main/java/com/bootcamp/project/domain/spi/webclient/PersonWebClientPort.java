package com.bootcamp.project.domain.spi.webclient;

import com.bootcamp.project.domain.model.webclient.person.api.ApiBootcampPersonList;
import reactor.core.publisher.Mono;

public interface PersonWebClientPort {
    Mono<ApiBootcampPersonList> getPersonsByBootcampsByIdMaxNumberPerson();
}
