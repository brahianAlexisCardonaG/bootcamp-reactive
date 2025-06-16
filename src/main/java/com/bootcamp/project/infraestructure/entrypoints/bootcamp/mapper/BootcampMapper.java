package com.bootcamp.project.infraestructure.entrypoints.bootcamp.mapper;

import com.bootcamp.project.domain.model.bootcamp.Bootcamp;
import com.bootcamp.project.infraestructure.entrypoints.bootcamp.dto.BootcampDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BootcampMapper {
    @Mapping(target = "id", ignore = true)
    Bootcamp toBootcamp(BootcampDto bootcampDto);
    BootcampDto toBootcampDto(Bootcamp bootcamp);
}
