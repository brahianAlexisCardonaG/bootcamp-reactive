package com.bootcamp.project.domain.spi.webclient;

import com.bootcamp.project.domain.model.webclient.capability.api.ApiCapabilityListTechnology;
import com.bootcamp.project.domain.model.webclient.capability.api.ApiCapabilityList;
import com.bootcamp.project.domain.model.webclient.capability.api.ApiCapabilityMessage;
import com.bootcamp.project.domain.model.webclient.capability.api.ApiMapCapability;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CapabilityWebClientPort {
    Mono<ApiMapCapability> getCapabilitiesByBootcampIds(List<Long> bootcampIds);

    Mono<ApiCapabilityList> getCapabilitiesByIds(List<Long> capabilityIds);

    Mono<ApiMapCapability> saveRelateCapabilitiesBootcamp(Long bootcampId,
                                                                       List<Long> capabilityIds);

    Mono<ApiCapabilityListTechnology> getCapabilitiesTechnologiesByIds(List<Long> capabilityIds);

    Mono<ApiCapabilityMessage> deleteBootcampCapabilities(List<Long> capabilityIds);
}
