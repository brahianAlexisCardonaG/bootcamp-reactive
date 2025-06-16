package com.bootcamp.project.infraestructure.entrypoints.personbootcamp.handler;

import com.bootcamp.project.domain.api.PersonBootcampServicePort;
import com.bootcamp.project.domain.enums.TechnicalMessage;
import com.bootcamp.project.infraestructure.entrypoints.personbootcamp.mapper.PersonBootcampMapper;
import com.bootcamp.project.infraestructure.entrypoints.personbootcamp.response.ApiPersonListBootcampCapTechResponse;
import com.bootcamp.project.infraestructure.entrypoints.util.error.ApplyErrorHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.time.Instant;

import static com.bootcamp.project.infraestructure.entrypoints.util.Constants.BOOTCAMP_ERROR;
import static com.bootcamp.project.infraestructure.entrypoints.util.Constants.X_MESSAGE_ID;

@Component
@RequiredArgsConstructor
@Slf4j
public class PersonBootcampHandlerImpl {
    private final PersonBootcampServicePort personBootcampServicePort;
    private final PersonBootcampMapper personBootcampMapper;
    private final ApplyErrorHandler applyErrorHandler;


    public Mono<ServerResponse> getPersonsByBootcampsByIdMaxNumberPerson(ServerRequest request) {
        Mono<ServerResponse> response = personBootcampServicePort.getPersonsByBootcampsByIdMaxNumberPerson()
                .map(personBootcampMapper::toPersonListBootcampCapTechResponse)
                .flatMap(bootcamp -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiPersonListBootcampCapTechResponse.builder()
                                .code(TechnicalMessage.BOOTCAMP_PERSON_MAX_NUMBER_PERSONS.getCode())
                                .message(TechnicalMessage.BOOTCAMP_PERSON_MAX_NUMBER_PERSONS.getMessage())
                                .date(Instant.now().toString())
                                .data(bootcamp)
                                .build()
                        )
                )
                .contextWrite(Context.of(X_MESSAGE_ID, ""))
                .doOnError(ex -> log.error(BOOTCAMP_ERROR, ex));

        return applyErrorHandler.applyErrorHandling(response);
    }
}
