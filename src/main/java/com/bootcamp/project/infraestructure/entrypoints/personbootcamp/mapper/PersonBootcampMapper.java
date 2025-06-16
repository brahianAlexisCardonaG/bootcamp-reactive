package com.bootcamp.project.infraestructure.entrypoints.personbootcamp.mapper;

import com.bootcamp.project.domain.model.personbootcamp.PersonListBootcampCapTech;
import com.bootcamp.project.infraestructure.entrypoints.personbootcamp.response.PersonListBootcampCapTechResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonBootcampMapper {
    PersonListBootcampCapTechResponse toPersonListBootcampCapTechResponse
            (PersonListBootcampCapTech personListBootcampCapTech);
}
