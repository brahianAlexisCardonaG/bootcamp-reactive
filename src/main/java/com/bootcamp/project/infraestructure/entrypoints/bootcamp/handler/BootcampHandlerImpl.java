package com.bootcamp.project.infraestructure.entrypoints.bootcamp.handler;

import com.bootcamp.project.domain.api.BootcampServicePort;
import com.bootcamp.project.domain.enums.TechnicalMessage;
import com.bootcamp.project.infraestructure.entrypoints.bootcamp.mapper.BootcampMapper;
import com.bootcamp.project.infraestructure.entrypoints.bootcamp.mapper.BootcampMapperResponse;
import com.bootcamp.project.infraestructure.entrypoints.bootcamp.response.api.ApiBootcampDeleteResponse;
import com.bootcamp.project.infraestructure.entrypoints.bootcamp.response.api.ApiResponseBase;
import com.bootcamp.project.infraestructure.entrypoints.util.error.ApplyErrorHandler;
import com.bootcamp.project.infraestructure.entrypoints.bootcamp.validate.ValidateRequestSave;
import com.bootcamp.project.infraestructure.entrypoints.bootcamp.response.api.ApiBootcampCapabilityTechnologyResponseList;
import com.bootcamp.project.infraestructure.entrypoints.bootcamp.response.BootcampListCapabilityTechnologyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static com.bootcamp.project.infraestructure.entrypoints.util.Constants.BOOTCAMP_ERROR;
import static com.bootcamp.project.infraestructure.entrypoints.util.Constants.X_MESSAGE_ID;

@Component
@RequiredArgsConstructor
@Slf4j
public class BootcampHandlerImpl {

    private final ValidateRequestSave validateRequestSave;
    private final BootcampMapper bootcampMapper;
    private final BootcampServicePort bootcampServicePort;
    private final ApplyErrorHandler applyErrorHandler;
    private final BootcampMapperResponse bootcampMapperResponse;

    public Mono<ServerResponse> createBootcampRelateCapabilities(ServerRequest request) {
        Mono<ServerResponse> response = validateRequestSave
                .validateAndMapRequest(request)            // Mono<SomeDto>
                .map(bootcampMapper::toBootcamp)
                .transform(bootcampServicePort::saveBootcampCapability)
                .flatMap(listData -> {
                    List<BootcampListCapabilityTechnologyResponse> responseList = listData.stream()
                            .map(bootcampMapperResponse::toBootcampListCapabilityTechnologyResponse)
                            .toList();
                    return ServerResponse.status(HttpStatus.CREATED)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(
                                    ApiBootcampCapabilityTechnologyResponseList.builder()
                                            .code(TechnicalMessage.BOOTCAMP_CAPABILITY_RELATION.getCode())
                                            .message(TechnicalMessage.BOOTCAMP_CAPABILITY_RELATION.getMessage())
                                            .date(Instant.now().toString())
                                            .data(responseList)
                                            .build()
                            );
                })
                .contextWrite(Context.of(X_MESSAGE_ID, ""))
                .doOnError(ex -> log.error(BOOTCAMP_ERROR, ex)).next();
        return applyErrorHandler.applyErrorHandling(response);
    }

    public Mono<ServerResponse> getCapabilitiesByBootcampsIds(ServerRequest request) {
        List<Long> ids = request.queryParams().getOrDefault("bootcampIds", List.of()).stream()
                .flatMap(p -> Arrays.stream(p.split("\\s*,\\s*")))
                .map(Long::parseLong)
                .toList();
        String order = request.queryParam("order").orElse("asc");
        int rows = Integer.parseInt(request.queryParam("rows").orElse("4"));
        int skip = Integer.parseInt(request.queryParam("skip").orElse("0"));

        Mono<ServerResponse> response = bootcampServicePort.findCapabilitiesByIdBootcamps(ids, order, skip, rows)
                .flatMap(listData -> {

                    List<BootcampListCapabilityTechnologyResponse> responseList = listData.stream()
                            .map(bootcampMapperResponse::toBootcampListCapabilityTechnologyResponse)
                            .toList();

                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(ApiBootcampCapabilityTechnologyResponseList.builder()
                                    .code(TechnicalMessage.BOOTCAMP_CAPABILITY_FOUND.getCode())
                                    .message(TechnicalMessage.BOOTCAMP_CAPABILITY_FOUND.getMessage())
                                    .date(Instant.now().toString())
                                    .data(responseList)
                                    .build());
                })
                .contextWrite(Context.of(X_MESSAGE_ID, ""))
                .doOnError(ex -> log.error( BOOTCAMP_ERROR, ex));

        return applyErrorHandler.applyErrorHandling(response);
    }

    public Mono<ServerResponse> deleteBootcampsByIds(ServerRequest request) {
        List<Long> ids = request.queryParams().getOrDefault("bootcampIds", List.of()).stream()
                .flatMap(p -> Arrays.stream(p.split("\\s*,\\s*")))
                .map(Long::parseLong)
                .toList();

        Mono<ServerResponse> response = bootcampServicePort.deleteBootcamps(ids)
                .then(ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(ApiBootcampDeleteResponse.builder()
                                    .code(TechnicalMessage.BOOTCAMPS_DELETE_SUCCESSFULLY.getCode())
                                    .message(TechnicalMessage.BOOTCAMPS_DELETE_SUCCESSFULLY.getMessage())
                                    .date(Instant.now().toString())
                                    .build()
                    )
                )
                .contextWrite(Context.of(X_MESSAGE_ID, ""))
                .doOnError(ex -> log.error( BOOTCAMP_ERROR, ex));

        return applyErrorHandler.applyErrorHandling(response);
    }

    public Mono<ServerResponse> getBootcampsByIds(ServerRequest request) {
        List<Long> ids = request.queryParams().getOrDefault("bootcampIds", List.of()).stream()
                .flatMap(p -> Arrays.stream(p.split("\\s*,\\s*")))
                .map(Long::parseLong)
                .toList();

        Mono<ServerResponse> response = bootcampServicePort.getBootcampsByIds(ids)
                .map(bootcampMapper::toBootcampDto)
                .collectList()
                .flatMap(boot -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponseBase.builder()
                                .code(TechnicalMessage.BOOTCAMP_CAPABILITY_FOUND.getCode())
                                .message(TechnicalMessage.BOOTCAMP_CAPABILITY_FOUND.getMessage())
                                .date(Instant.now().toString())
                                .data(boot)
                                .build()
                        )
                )
                .contextWrite(Context.of(X_MESSAGE_ID, ""))
                .doOnError(ex -> log.error( BOOTCAMP_ERROR, ex));

        return applyErrorHandler.applyErrorHandling(response);
    }

}
