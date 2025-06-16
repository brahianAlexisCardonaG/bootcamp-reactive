package com.bootcamp.project.domain.spi.webclient;

import com.bootcamp.project.domain.model.webclient.technology.api.ApiTechnologyMap;
import com.bootcamp.project.domain.model.webclient.technology.api.ApiTechnologyMessage;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TechnologyWebClientPort {
    Mono<ApiTechnologyMessage> deleteCapabilityTechnologies(List<Long> TechnologyIds);
    Mono<ApiTechnologyMap> getTechnologiesByCapabilityIds(List<Long> capabilityIds);
}
