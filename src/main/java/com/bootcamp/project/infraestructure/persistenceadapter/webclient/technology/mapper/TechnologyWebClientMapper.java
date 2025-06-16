package com.bootcamp.project.infraestructure.persistenceadapter.webclient.technology.mapper;

import com.bootcamp.project.domain.model.webclient.technology.Technology;
import com.bootcamp.project.domain.model.webclient.technology.api.ApiTechnologyMap;
import com.bootcamp.project.domain.model.webclient.technology.api.ApiTechnologyMessage;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.technology.response.TechnologyResponse;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.technology.response.api.ApiTechnologyMapResponse;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.technology.response.api.ApiTechnologyMessageResponse;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TechnologyWebClientMapper {
    ApiTechnologyMap toApiTechnologyMap(ApiTechnologyMapResponse apiTechnologyMapResponse);
    ApiTechnologyMessage toApiTechnologyMessage(ApiTechnologyMessageResponse apiTechnologyMessageResponse);

    Technology toTechnology(TechnologyResponse technologyResponse);

    // Método para mapear el Map de listas
    default Map<String, List<Technology>> map(Map<String, List<TechnologyResponse>> technologyResponseMap) {
        if (technologyResponseMap == null) {
            return null;
        }
        return technologyResponseMap.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue()
                                .stream()
                                .map(this::toTechnology)
                                .collect(Collectors.toList())
                ));
    }
}
