package com.bootcamp.project.infraestructure.persistenceadapter.webclient.person.mapper;

import com.bootcamp.project.domain.model.webclient.person.BootcampPersonList;
import com.bootcamp.project.domain.model.webclient.person.api.ApiBootcampPersonList;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.person.response.api.ApiBootcampPersonListResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonWebClientMapper {
    ApiBootcampPersonList toApiBootcampPersonList(ApiBootcampPersonListResponse apiBootcampPersonListResponse);
}
