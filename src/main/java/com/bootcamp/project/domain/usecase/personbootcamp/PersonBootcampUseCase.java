package com.bootcamp.project.domain.usecase.personbootcamp;

import com.bootcamp.project.domain.api.PersonBootcampServicePort;
import com.bootcamp.project.domain.model.personbootcamp.PersonListBootcampCapTech;
import com.bootcamp.project.domain.model.webclient.capability.Capability;
import com.bootcamp.project.domain.model.webclient.capability.CapabilityTechnology;
import com.bootcamp.project.domain.model.webclient.person.BootcampPersonList;
import com.bootcamp.project.domain.spi.webclient.CapabilityWebClientPort;
import com.bootcamp.project.domain.spi.webclient.PersonWebClientPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PersonBootcampUseCase implements PersonBootcampServicePort {
    private final PersonWebClientPort personWebClientPort;
    private final CapabilityWebClientPort capabilityWebClientPort;

    @Override
    public Mono<PersonListBootcampCapTech> getPersonsByBootcampsByIdMaxNumberPerson() {
        return personWebClientPort.getPersonsByBootcampsByIdMaxNumberPerson()
                .flatMap(apiBootcampPersonList -> {
                    // Extraemos la información del bootcamp
                    BootcampPersonList bootcamp = apiBootcampPersonList.getData();
                    if (bootcamp == null || bootcamp.getIdBootcamp() == null) {
                        return Mono.error(new RuntimeException("No se encontró información del Bootcamp"));
                    }
                    Long bootcampId = bootcamp.getIdBootcamp();

                    // Consultamos las capabilities asociadas al bootcamp.
                    // Se asume que la llave en el Map es el idBootcamp convertido a String.
                    return capabilityWebClientPort.getCapabilitiesByBootcampIds(Collections.singletonList(bootcampId))
                            .flatMap(apiMapCapability -> {
                                Map<String, List<Capability>> capMap = apiMapCapability.getData();
                                List<Capability> capabilities = capMap.get(bootcampId.toString());

                                // Si no se encontraron capabilities, se retorna la data sin capabilities.
                                if (capabilities == null || capabilities.isEmpty()) {
                                    PersonListBootcampCapTech result = PersonListBootcampCapTech.builder()
                                            .id(bootcamp.getIdBootcamp())
                                            .name(bootcamp.getName())
                                            .releaseDate(bootcamp.getReleaseDate())
                                            .duration(bootcamp.getDuration())
                                            .persons(bootcamp.getPersons())
                                            .capabilities(Collections.emptyList())
                                            .build();
                                    return Mono.just(result);
                                }

                                // Extraemos los ids de las capabilities
                                List<Long> capabilityIds = capabilities.stream()
                                        .map(Capability::getId)
                                        .collect(Collectors.toList());

                                // Con los capabilityIds, consultamos las technologies asociadas a cada capability
                                return capabilityWebClientPort.getCapabilitiesTechnologiesByIds(capabilityIds)
                                        .map(apiCapabilityListTechnology -> {
                                            List<CapabilityTechnology> capabilityTechnologyList = apiCapabilityListTechnology.getData();
                                            return PersonListBootcampCapTech.builder()
                                                    .id(bootcamp.getIdBootcamp())
                                                    .name(bootcamp.getName())
                                                    .releaseDate(bootcamp.getReleaseDate())
                                                    .duration(bootcamp.getDuration())
                                                    .persons(bootcamp.getPersons())
                                                    .capabilities(capabilityTechnologyList)
                                                    .build();
                                        });
                            });
                });
    }
}
