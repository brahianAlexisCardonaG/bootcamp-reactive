package com.bootcamp.project.infraestructure.persistenceadapter.webclient.person;

import com.bootcamp.project.domain.model.webclient.person.api.ApiBootcampPersonList;
import com.bootcamp.project.domain.model.webclient.technology.api.ApiTechnologyMap;
import com.bootcamp.project.domain.model.webclient.technology.api.ApiTechnologyMessage;
import com.bootcamp.project.domain.spi.webclient.PersonWebClientPort;
import com.bootcamp.project.domain.spi.webclient.TechnologyWebClientPort;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.person.mapper.PersonWebClientMapper;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.person.response.api.ApiBootcampPersonListResponse;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.technology.mapper.TechnologyWebClientMapper;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.technology.response.api.ApiTechnologyMapResponse;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.technology.response.api.ApiTechnologyMessageResponse;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.util.ErrorsWebClient;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.util.SendTokenWebClient;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonClient implements PersonWebClientPort {
    private final WebClient webClient;
    private final PersonWebClientMapper personWebClientMapper;

    public PersonClient(WebClient.Builder builder,
                        SendTokenWebClient sendTokenWebClient,
                        PersonWebClientMapper personWebClientMapper) {
        this.webClient = builder.baseUrl("http://localhost:8084")
                .filter(sendTokenWebClient.authHeaderFilter())
                .build();
        this.personWebClientMapper = personWebClientMapper;
    }

    @Override
    public Mono<ApiBootcampPersonList> getPersonsByBootcampsByIdMaxNumberPerson() {
        String url = "/api/v1/person/bootcamp/get-bootcamp-person";

        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(url)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> ErrorsWebClient
                        .handleError(response.bodyToMono(String.class)))
                .bodyToMono(ApiBootcampPersonListResponse.class)
                .map(personWebClientMapper::toApiBootcampPersonList);
    }
}
