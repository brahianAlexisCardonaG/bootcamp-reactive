package com.bootcamp.project.infraestructure.persistenceadapter.webclient.capability.mapper;

import com.bootcamp.project.domain.model.webclient.capability.Capability;
import com.bootcamp.project.domain.model.webclient.capability.api.*;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.capability.response.CapabilityResponse;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.capability.response.api.ApiCapabilityListTechnologyResponse;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.capability.response.api.ApiMapCapabilityResponse;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.capability.response.api.ApiCapabilityListResponse;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.capability.response.api.ApiCapabilityMessageResponse;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CapabilityWebClientMapper {
    ApiMapCapability toApiBootcampCapability(ApiMapCapabilityResponse apiMapCapabilityResponse);
    ApiCapabilityList toApiCapabilityList(ApiCapabilityListResponse apiCapabilityListResponse);
    ApiCapabilityListTechnology toApiCapabilityListTechnology
            (ApiCapabilityListTechnologyResponse apiCapabilityListTechnologyResponse);
    ApiCapabilityMessage toApiCapabilityMessage(ApiCapabilityMessageResponse apiCapabilityMessageResponse);

    Capability toCapability(CapabilityResponse capabilityResponse);
    // Metodo para mapear el Map de listas
    default Map<String, List<Capability>> map(Map<String, List<CapabilityResponse>> capabilityResponseMap) {
        if (capabilityResponseMap == null) {
            return null;
        }
        return capabilityResponseMap.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue()
                                .stream()
                                .map(this::toCapability)
                                .collect(Collectors.toList())
                ));
    }

}