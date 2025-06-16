package com.bootcamp.project.infraestructure.persistenceadapter.webclient.technology;

import com.bootcamp.project.domain.model.webclient.technology.api.ApiTechnologyMap;
import com.bootcamp.project.domain.model.webclient.technology.api.ApiTechnologyMessage;
import com.bootcamp.project.domain.spi.webclient.TechnologyWebClientPort;
import com.bootcamp.project.infraestructure.entrypoints.bootcamp.response.api.ApiResponseBase;
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
public class TechnologyClient implements TechnologyWebClientPort {
    private final WebClient webClient;
    private final TechnologyWebClientMapper technologyWebClientMapper;

    public TechnologyClient(WebClient.Builder builder,
                            SendTokenWebClient sendTokenWebClient,
                            TechnologyWebClientMapper technologyWebClientMapper) {
        this.webClient = builder.baseUrl("http://localhost:8081")
                .filter(sendTokenWebClient.authHeaderFilter())
                .build();
        this.technologyWebClientMapper = technologyWebClientMapper;
    }

    @Override
    public Mono<ApiTechnologyMap> getTechnologiesByCapabilityIds(List<Long> capabilityIds) {
        String url = "/api/v1/technology/by-capabilities-ids";

        String idsParam = capabilityIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(url)
                        .queryParam("capabilityIds", idsParam)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> ErrorsWebClient
                        .handleError(response.bodyToMono(String.class)))
                .bodyToMono(ApiTechnologyMapResponse.class)
                .map(technologyWebClientMapper::toApiTechnologyMap);
    }

    @Override
    public Mono<ApiTechnologyMessage> deleteCapabilityTechnologies(List<Long> TechnologyIds) {
        String url = "/api/v1/technology/capability/delete";

        String idsParam = TechnologyIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        return webClient.delete()
                .uri(uriBuilder -> uriBuilder.path(url)
                        .queryParam("technologyIds", idsParam)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> ErrorsWebClient
                        .handleError(response.bodyToMono(String.class)))
                .bodyToMono(ApiTechnologyMessageResponse.class)
                .map(technologyWebClientMapper::toApiTechnologyMessage);
    }

}
