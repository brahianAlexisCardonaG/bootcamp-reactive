package com.bootcamp.project.infraestructure.persistenceadapter.bootcamp.mapper;

import com.bootcamp.project.domain.model.bootcamp.Bootcamp;
import com.bootcamp.project.infraestructure.persistenceadapter.bootcamp.entity.BootcampEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BootcampEntityMapper {
    @Mapping(target = "capabilityIds", ignore = true)
    Bootcamp toModel(BootcampEntity bootcampEntity);
    BootcampEntity toEntity(Bootcamp bootcamp);
}
