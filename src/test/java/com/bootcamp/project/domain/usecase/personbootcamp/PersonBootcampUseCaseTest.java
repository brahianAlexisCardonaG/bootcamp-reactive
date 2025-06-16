package com.bootcamp.project.domain.usecase.personbootcamp;

import com.bootcamp.project.domain.model.webclient.capability.Capability;
import com.bootcamp.project.domain.model.webclient.capability.CapabilityTechnology;
import com.bootcamp.project.domain.model.webclient.capability.api.ApiCapabilityListTechnology;
import com.bootcamp.project.domain.model.webclient.capability.api.ApiMapCapability;
import com.bootcamp.project.domain.model.webclient.person.BootcampPersonList;
import com.bootcamp.project.domain.model.webclient.person.Person;
import com.bootcamp.project.domain.model.webclient.person.api.ApiBootcampPersonList;
import com.bootcamp.project.domain.model.webclient.technology.Technology;
import com.bootcamp.project.domain.spi.webclient.CapabilityWebClientPort;
import com.bootcamp.project.domain.spi.webclient.PersonWebClientPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PersonBootcampUseCaseTest {
    @Mock
    private PersonWebClientPort personWebClientPort;

    @Mock
    private CapabilityWebClientPort capabilityWebClientPort;

    @InjectMocks
    private PersonBootcampUseCase personBootcampUseCase;

    @BeforeEach
    void setUp() {
        personBootcampUseCase = new PersonBootcampUseCase(personWebClientPort, capabilityWebClientPort);
    }

    @Test
    void getPersonsByBootcampsByIdMaxNumberPerson_success() {
        // Arrange
        Long bootcampId = 101L;

        BootcampPersonList bootcamp = BootcampPersonList.builder()
                .idBootcamp(bootcampId)
                .name("Bootcamp Reactivo")
                .releaseDate(LocalDate.of(2025, 1, 1))
                .duration(30)
                .persons(List.of(new Person(1L, "Ana", "ana@example.com")))
                .build();

        ApiBootcampPersonList apiBootcampPersonList = ApiBootcampPersonList.builder()
                .code("200")
                .message("OK")
                .date("2025-01-01")
                .data(bootcamp)
                .build();

        List<Capability> capabilities = List.of(
                new Capability(201L, "Java"),
                new Capability(202L, "Spring")
        );

        Map<String, List<Capability>> capabilityMap = Map.of(
                bootcampId.toString(), capabilities
        );

        List<CapabilityTechnology> capabilityTechnologies = List.of(
                new CapabilityTechnology(201L, "Java", List.of(new Technology(301L, "JDK"))),
                new CapabilityTechnology(202L, "Spring", List.of(new Technology(302L, "Spring WebFlux")))
        );

        ApiMapCapability apiMapCapability = ApiMapCapability.builder()
                .code("200")
                .message("OK")
                .date("2025-01-01")
                .data(capabilityMap)
                .build();

        ApiCapabilityListTechnology apiCapabilityListTechnology = ApiCapabilityListTechnology.builder()
                .code("200")
                .message("OK")
                .date("2025-01-01")
                .data(capabilityTechnologies)
                .build();

        when(personWebClientPort.getPersonsByBootcampsByIdMaxNumberPerson())
                .thenReturn(Mono.just(apiBootcampPersonList));

        when(capabilityWebClientPort.getCapabilitiesByBootcampIds(List.of(bootcampId)))
                .thenReturn(Mono.just(apiMapCapability));

        when(capabilityWebClientPort.getCapabilitiesTechnologiesByIds(List.of(201L, 202L)))
                .thenReturn(Mono.just(apiCapabilityListTechnology));

        // Act & Assert
        StepVerifier.create(personBootcampUseCase.getPersonsByBootcampsByIdMaxNumberPerson())
                .assertNext(result -> {
                    assert result.getId().equals(bootcampId);
                    assert result.getName().equals("Bootcamp Reactivo");
                    assert result.getDuration().equals(30);
                    assert result.getPersons().size() == 1;
                    assert result.getCapabilities().size() == 2;
                    assert result.getCapabilities().get(0).getTechnologies().size() == 1;
                })
                .verifyComplete();

        verify(personWebClientPort).getPersonsByBootcampsByIdMaxNumberPerson();
        verify(capabilityWebClientPort).getCapabilitiesByBootcampIds(List.of(bootcampId));
        verify(capabilityWebClientPort).getCapabilitiesTechnologiesByIds(List.of(201L, 202L));
    }
}
