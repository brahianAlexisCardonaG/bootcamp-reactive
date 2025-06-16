package com.bootcamp.project.infraestructure.persistenceadapter.webclient.person;

import com.bootcamp.project.domain.model.webclient.person.BootcampPersonList;
import com.bootcamp.project.domain.model.webclient.person.Person;
import com.bootcamp.project.domain.model.webclient.person.api.ApiBootcampPersonList;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.person.mapper.PersonWebClientMapper;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.person.response.BootcampPersonListResponse;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.person.response.PersonResponse;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.person.response.api.ApiBootcampPersonListResponse;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.util.SendTokenWebClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PersonClientTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private PersonWebClientMapper personWebClientMapper;

    private PersonClient personClient;

    @BeforeEach
    void setUp() {
        when(webClientBuilder.baseUrl(any())).thenReturn(webClientBuilder);
        when(webClientBuilder.filter(any())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        SendTokenWebClient sendTokenWebClient = new SendTokenWebClient();
        personClient = new PersonClient(webClientBuilder, sendTokenWebClient, personWebClientMapper);
    }

    @Test
    void testGetPersonsByBootcampsByIdMaxNumberPerson() {
        // Mock response desde el servicio externo (DTO)
        BootcampPersonListResponse bootcampPersonListResponse = BootcampPersonListResponse.builder()
                .idBootcamp(1L)
                .name("Bootcamp Java")
                .releaseDate(LocalDate.of(2024, 1, 1))
                .duration(12)
                .persons(List.of(
                        new PersonResponse(100L, "John Doe", "john@example.com"),
                        new PersonResponse(101L, "Jane Roe", "jane@example.com")
                ))
                .build();

        ApiBootcampPersonListResponse apiResponse = ApiBootcampPersonListResponse.builder()
                .code("200")
                .message("Success")
                .date("2025-06-13")
                .data(bootcampPersonListResponse)
                .build();

        // Mock resultado esperado mapeado (dominio)
        BootcampPersonList bootcampPersonList = BootcampPersonList.builder()
                .idBootcamp(1L)
                .name("Bootcamp Java")
                .releaseDate(LocalDate.of(2024, 1, 1))
                .duration(12)
                .persons(List.of(
                        Person.builder().id(100L).name("John Doe").email("john@example.com").build(),
                        Person.builder().id(101L).name("Jane Roe").email("jane@example.com").build()
                ))
                .build();

        ApiBootcampPersonList expectedDomain = ApiBootcampPersonList.builder()
                .code("200")
                .message("Success")
                .date("2025-06-13")
                .data(bootcampPersonList)
                .build();

        // Mock flujo WebClient
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ApiBootcampPersonListResponse.class)).thenReturn(Mono.just(apiResponse));

        when(personWebClientMapper.toApiBootcampPersonList(apiResponse)).thenReturn(expectedDomain);
        Mono<ApiBootcampPersonList> result = personClient.getPersonsByBootcampsByIdMaxNumberPerson();

        StepVerifier.create(result)
                .expectNext(expectedDomain)
                .verifyComplete();

        verify(webClient).get();
        verify(requestHeadersUriSpec).uri(any(Function.class));
        verify(requestHeadersSpec).retrieve();
        verify(responseSpec).bodyToMono(ApiBootcampPersonListResponse.class);
        verify(personWebClientMapper).toApiBootcampPersonList(apiResponse);
    }
}
