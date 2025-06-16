package com.bootcamp.project.infraestructure.persistenceadapter.webclient.capability;

import com.bootcamp.project.domain.model.webclient.capability.api.*;
import com.bootcamp.project.domain.spi.webclient.CapabilityWebClientPort;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.capability.mapper.CapabilityWebClientMapper;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.capability.response.api.ApiCapabilityListTechnologyResponse;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.capability.response.api.ApiCapabilityMessageResponse;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.capability.response.api.ApiMapCapabilityResponse;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.capability.response.api.ApiCapabilityListResponse;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.util.ErrorsWebClient;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.util.SendTokenWebClient;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CapabilityClient implements CapabilityWebClientPort {
    private final WebClient webClient;
    private final CapabilityWebClientMapper capabilityWebClientMapper;

    public CapabilityClient(WebClient.Builder builder,
                            SendTokenWebClient sendTokenWebClient,
                            CapabilityWebClientMapper capabilityWebClientMapper) {
        this.webClient = builder.baseUrl("http://localhost:8082")
                .filter(sendTokenWebClient.authHeaderFilter())
                .build();
        this.capabilityWebClientMapper = capabilityWebClientMapper;
    }


    @Override
    public Mono<ApiMapCapability> getCapabilitiesByBootcampIds(List<Long> bootcampIds) {

        String idsParam = bootcampIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("api/v1/capability/by-bootcamp-ids")
                .queryParam("bootcampIds", idsParam)
                .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        ErrorsWebClient.handleError(response.bodyToMono(String.class)))
                .bodyToMono(ApiMapCapabilityResponse.class)
                .map(capabilityWebClientMapper::toApiBootcampCapability);
    }

    @Override
    public Mono<ApiCapabilityList> getCapabilitiesByIds(List<Long> capabilityIds) {
        String idsParam = capabilityIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("api/v1/capability")
                        .queryParam("ids", idsParam)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        ErrorsWebClient.handleError(response.bodyToMono(String.class)))
                .bodyToMono(ApiCapabilityListResponse.class)
                .map(capabilityWebClientMapper::toApiCapabilityList);
    }

    @Override
    public Mono<ApiMapCapability> saveRelateCapabilitiesBootcamp(Long bootcampId, List<Long> capabilityIds) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("bootcampId", bootcampId);
        requestBody.put("capabilityIds", capabilityIds);

        return webClient.post()
                .uri("api/v1/capability-bootcamp")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        ErrorsWebClient.handleError(response.bodyToMono(String.class)))
                .bodyToMono(ApiMapCapabilityResponse.class)
                .map(capabilityWebClientMapper::toApiBootcampCapability);
    }

    @Override
    public Mono<ApiCapabilityListTechnology> getCapabilitiesTechnologiesByIds(List<Long> capabilityIds) {
        String idsParam = capabilityIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("api/v1/capability/find-technologies")
                        .queryParam("capabilityIds", idsParam)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        ErrorsWebClient.handleError(response.bodyToMono(String.class)))
                .bodyToMono(ApiCapabilityListTechnologyResponse.class)
                .map(capabilityWebClientMapper::toApiCapabilityListTechnology);
    }

    @Override
    public Mono<ApiCapabilityMessage> deleteBootcampCapabilities(List<Long> capabilityIds) {
        String idsParam = capabilityIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        return webClient.delete()
                .uri(uriBuilder -> uriBuilder.path("api/v1/capability/bootcamp/delete")
                        .queryParam("capabilityIds", idsParam)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        ErrorsWebClient.handleError(response.bodyToMono(String.class)))
                .bodyToMono(ApiCapabilityMessageResponse.class)
                .map(capabilityWebClientMapper::toApiCapabilityMessage);
    }
}
