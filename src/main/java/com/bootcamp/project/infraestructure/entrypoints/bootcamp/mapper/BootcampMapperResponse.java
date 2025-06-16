package com.bootcamp.project.infraestructure.entrypoints.bootcamp.mapper;

import com.bootcamp.project.domain.model.bootcamp.BootcampListCapabilityTechnology;
import com.bootcamp.project.infraestructure.entrypoints.bootcamp.response.BootcampListCapabilityTechnologyResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BootcampMapperResponse {
    BootcampListCapabilityTechnologyResponse toBootcampListCapabilityTechnologyResponse
            (BootcampListCapabilityTechnology bootcampListCapabilityTechnology);


}
