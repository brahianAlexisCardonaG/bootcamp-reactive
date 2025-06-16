package com.bootcamp.project.infraestructure.entrypoints.bootcamp.validate;

import com.bootcamp.project.domain.enums.TechnicalMessage;
import com.bootcamp.project.domain.exception.BusinessException;
import com.bootcamp.project.infraestructure.entrypoints.bootcamp.dto.BootcampDto;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class BootcampValidationDto {
    public Mono<BootcampDto> validateLengthWords(BootcampDto dto) {
        if (dto.getName().length() > 50) {
            return Mono.error(new BusinessException(TechnicalMessage.NAME_TOO_LONG));
        }
        return Mono.just(dto);
    }

    public Mono<List<BootcampDto>> validateNoDuplicateNames(List<BootcampDto> dtoList) {
        Set<String> names = new HashSet<>();
        List<String> duplicatedNames = dtoList.stream()
                .map(BootcampDto::getName)
                .filter(name -> !names.add(name)) // Si no se puede agregar al set, es duplicado
                .toList();

        if (!duplicatedNames.isEmpty()) {
            return Mono.error(new BusinessException(TechnicalMessage.DUPLICATE_NAMES_BOOTCAMP));
        }

        return Mono.just(dtoList);
    }

    public Mono<BootcampDto> validateFieldNotNullOrBlank(BootcampDto dto) {
        if (dto.getName() == null || dto.getDuration() == null  || dto.getCapabilityIds().isEmpty()
        || dto.getReleaseDate() == null) {
            return Mono.error(new BusinessException(TechnicalMessage.INVALID_PARAMETERS));
        }
        return Mono.just(dto);
    }
}
