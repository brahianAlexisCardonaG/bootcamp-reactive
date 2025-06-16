package com.bootcamp.project.infraestructure.persistenceadapter.mongodb.mapper;

import com.bootcamp.project.domain.model.bootcampmongo.BootcampMongo;
import com.bootcamp.project.infraestructure.persistenceadapter.mongodb.entity.BootcampMongoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BootcampMongoEntityMapper {
    BootcampMongoEntity toEntity(BootcampMongo bootcampMongo);
}
