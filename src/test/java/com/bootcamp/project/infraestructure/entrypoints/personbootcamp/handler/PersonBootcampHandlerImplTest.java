package com.bootcamp.project.infraestructure.entrypoints.personbootcamp.handler;

import com.bootcamp.project.domain.api.PersonBootcampServicePort;
import com.bootcamp.project.domain.enums.TechnicalMessage;
import com.bootcamp.project.domain.model.personbootcamp.PersonListBootcampCapTech;
import com.bootcamp.project.infraestructure.entrypoints.personbootcamp.mapper.PersonBootcampMapper;
import com.bootcamp.project.infraestructure.entrypoints.personbootcamp.response.ApiPersonListBootcampCapTechResponse;
import com.bootcamp.project.infraestructure.entrypoints.personbootcamp.response.PersonListBootcampCapTechResponse;
import com.bootcamp.project.infraestructure.entrypoints.util.error.ApplyErrorHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonBootcampHandlerImplTest {

    @InjectMocks
    private PersonBootcampHandlerImpl handler;

    @Mock
    private PersonBootcampServicePort servicePort;

    @Mock
    private PersonBootcampMapper mapper;

    @Mock
    private ApplyErrorHandler applyErrorHandler;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        RouterFunction<ServerResponse> route = RouterFunctions.route(
                RequestPredicates.GET("/api/v1/person-bootcamp/max-persons")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                handler::getPersonsByBootcampsByIdMaxNumberPerson
        );
        webTestClient = WebTestClient.bindToRouterFunction(route).build();
    }

    @Test
    void testGetPersonsByBootcampsByIdMaxNumberPerson_Success() {
        // Arrange
        PersonListBootcampCapTech domainObject = PersonListBootcampCapTech.builder()
                .id(1L)
                .name("Bootcamp Java")
                .releaseDate(LocalDate.now())
                .duration(60)
                .persons(Collections.emptyList())
                .capabilities(Collections.emptyList())
                .build();

        PersonListBootcampCapTechResponse dtoResponse = new PersonListBootcampCapTechResponse();
        dtoResponse.setId(1L);
        dtoResponse.setName("Bootcamp Java");
        dtoResponse.setReleaseDate(domainObject.getReleaseDate());
        dtoResponse.setDuration(60);
        dtoResponse.setPersons(Collections.emptyList());
        dtoResponse.setCapabilities(Collections.emptyList());

        when(servicePort.getPersonsByBootcampsByIdMaxNumberPerson())
                .thenReturn(Mono.just(domainObject));

        when(mapper.toPersonListBootcampCapTechResponse(domainObject))
                .thenReturn(dtoResponse);

        when(applyErrorHandler.applyErrorHandling(any(Mono.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act & Assert
        webTestClient.get()
                .uri("/api/v1/person-bootcamp/max-persons")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ApiPersonListBootcampCapTechResponse.class)
                .consumeWith(response -> {
                    ApiPersonListBootcampCapTechResponse body = response.getResponseBody();
                    assert body != null;
                    assert body.getCode().equals(TechnicalMessage.BOOTCAMP_PERSON_MAX_NUMBER_PERSONS.getCode());
                    assert body.getMessage().equals(TechnicalMessage.BOOTCAMP_PERSON_MAX_NUMBER_PERSONS.getMessage());
                    assert body.getData().getId().equals(1L);
                });

        // Verify interactions
        verify(servicePort, times(1)).getPersonsByBootcampsByIdMaxNumberPerson();
        verify(mapper, times(1)).toPersonListBootcampCapTechResponse(any());
        verify(applyErrorHandler, times(1)).applyErrorHandling(any(Mono.class));
    }

}
