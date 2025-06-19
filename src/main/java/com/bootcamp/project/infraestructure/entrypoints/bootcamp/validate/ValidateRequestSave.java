package com.bootcamp.project.infraestructure.entrypoints.bootcamp.validate;

import com.bootcamp.project.infraestructure.entrypoints.bootcamp.dto.BootcampDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class ValidateRequestSave {
    private final BootcampValidationDto bootcampValidationDto;

    public Flux<BootcampDto> validateAndMapRequest(ServerRequest request) {
        return request.bodyToFlux(BootcampDto.class)
                .collectList()
                .flatMapMany(dtoList ->
                        bootcampValidationDto.validateNoDuplicateNames(dtoList)
                                .thenMany(Flux.fromIterable(dtoList))
                )
                .flatMap(dto ->
                        bootcampValidationDto.validateLengthWords(dto)
                                .then(bootcampValidationDto.validateFieldNotNullOrBlank(dto))
                                .thenReturn(dto)
                );
    }
}
