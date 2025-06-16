package com.bootcamp.project.domain.usecase.bootcamp;

import com.bootcamp.project.domain.enums.TechnicalMessage;
import com.bootcamp.project.domain.exception.BusinessException;
import com.bootcamp.project.domain.model.bootcamp.Bootcamp;
import com.bootcamp.project.domain.model.bootcampmongo.BootcampMongo;
import com.bootcamp.project.domain.model.webclient.capability.Capability;
import com.bootcamp.project.domain.model.webclient.capability.api.ApiCapabilityList;
import com.bootcamp.project.domain.model.webclient.technology.Technology;
import com.bootcamp.project.domain.model.webclient.capability.api.ApiCapabilityListTechnology;
import com.bootcamp.project.domain.model.webclient.capability.api.ApiMapCapability;
import com.bootcamp.project.domain.model.bootcamp.BootcampListCapabilityTechnology;
import com.bootcamp.project.domain.model.webclient.capability.CapabilityTechnology;
import com.bootcamp.project.domain.model.webclient.technology.api.ApiTechnologyMap;
import com.bootcamp.project.domain.spi.bootcamp.BootcampPersistencePort;
import com.bootcamp.project.domain.spi.bootcampmongo.BootcampMongoPersistencePort;
import com.bootcamp.project.domain.spi.webclient.CapabilityWebClientPort;
import com.bootcamp.project.domain.spi.webclient.TechnologyWebClientPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BootcampUseCaseTest {
    @Mock
    private BootcampPersistencePort bootcampPersistencePort;

    @Mock
    private CapabilityWebClientPort capabilityWebClientPort;

    @Mock
    private TechnologyWebClientPort technologyWebClientPort;

    @Mock
    private TransactionalOperator transactionalOperator;

    @Mock
    private BootcampMongoPersistencePort bootcampMongoPersistencePort;

    private BootcampUseCase bootcampUseCase;

    @BeforeEach
    void setup() {
        bootcampUseCase = new BootcampUseCase(
                bootcampPersistencePort,
                transactionalOperator,
                capabilityWebClientPort,
                technologyWebClientPort,
                bootcampMongoPersistencePort
        );
    }

    @Test
    void saveBootcampCapability_success() {
        // Arrange
        List<Long> capabilityIds = List.of(1L, 2L);
        Bootcamp bootcamp = Bootcamp.builder()
                .id(null)
                .name("Bootcamp 1")
                .releaseDate(LocalDate.now())
                .duration(10)
                .capabilityIds(capabilityIds)
                .build();

        Bootcamp savedBootcamp = Bootcamp.builder()
                .id(100L)
                .name("Bootcamp 1")
                .releaseDate(bootcamp.getReleaseDate())
                .duration(bootcamp.getDuration())
                .capabilityIds(capabilityIds)
                .build();

        List<Capability> capabilityList = List.of(
                new Capability(1L, "Cap1"),
                new Capability(2L, "Cap2")
        );

        ApiCapabilityList capabilityListResponse = ApiCapabilityList.builder()
                .code("200")
                .message("Success")
                .date("2025-06-05")
                .data(capabilityList)
                .build();

        List<CapabilityTechnology> capabilityTechnologyList = List.of(
                new CapabilityTechnology(1L, "Cap1", Collections.emptyList()),
                new CapabilityTechnology(2L, "Cap2", Collections.emptyList())
        );

        ApiCapabilityListTechnology techResponse = ApiCapabilityListTechnology.builder()
                .code("200")
                .message("Success")
                .date("2025-06-05")
                .data(capabilityTechnologyList)
                .build();

        BootcampMongo mongoEntity = BootcampMongo.builder()
                .id(savedBootcamp.getId())
                .idBootcamp(savedBootcamp.getId())
                .name(savedBootcamp.getName())
                .releaseDate(savedBootcamp.getReleaseDate())
                .duration(savedBootcamp.getDuration())
                .numberCapabilities(2)
                .numberTechnologies(0)
                .numberPersons(0)
                .build();

        // 1. Validar que el Bootcamp no exista
        when(bootcampPersistencePort.findByName("Bootcamp 1"))
                .thenReturn(Mono.just(false));

        // 2. Stub para validar capabilities existentes
        when(capabilityWebClientPort.getCapabilitiesByIds(capabilityIds))
                .thenReturn(Mono.just(capabilityListResponse));

        // 3. Guardar el Bootcamp
        when(bootcampPersistencePort.save(any(Flux.class)))
                .thenReturn(Flux.just(savedBootcamp));

        // 4. Guardar relaciones
        when(capabilityWebClientPort.saveRelateCapabilitiesBootcamp(eq(100L), eq(capabilityIds)))
                .thenReturn(Mono.empty());

        // 5. Obtener tecnologías por capability
        when(capabilityWebClientPort.getCapabilitiesTechnologiesByIds(eq(capabilityIds)))
                .thenReturn(Mono.just(techResponse));

        // 6. Guardar en Mongo
        when(bootcampMongoPersistencePort.saveAll(any(Flux.class)))
                .thenReturn(Mono.empty());

        // 7. Transacción
        when(transactionalOperator.transactional(any(Mono.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act & Assert
        StepVerifier.create(bootcampUseCase.saveBootcampCapability(Flux.just(bootcamp)))
                .assertNext(list -> {
                    assert list.size() == 1;
                    BootcampListCapabilityTechnology result = list.get(0);
                    assert result.getId().equals(100L);
                    assert result.getName().equals("Bootcamp 1");
                    assert result.getCapabilities() != null;
                    assert result.getCapabilities().size() == 2;
                })
                .verifyComplete();

        // Verificar mocks
        verify(bootcampPersistencePort).findByName("Bootcamp 1");
        verify(bootcampPersistencePort).save(any(Flux.class));
        verify(capabilityWebClientPort).getCapabilitiesByIds(capabilityIds);
        verify(capabilityWebClientPort).saveRelateCapabilitiesBootcamp(eq(100L), eq(capabilityIds));
        verify(capabilityWebClientPort).getCapabilitiesTechnologiesByIds(capabilityIds);
        verify(bootcampMongoPersistencePort).saveAll(any(Flux.class));
    }

    @Test
    void findCapabilitiesByIdBootcamps_Success() {
        // Datos de prueba
        List<Long> bootcampIds = List.of(1L, 2L);

        List<Bootcamp> bootcamps = List.of(
                new Bootcamp(1L, "Bootcamp 1", null, 0, null),
                new Bootcamp(2L, "Bootcamp 2", null, 0, null)
        );

        // CORRECTO: usamos ApiBootcampCapabilityResponse con Map<String, List<CapabilityResponse>>
        ApiMapCapability capabilities = ApiMapCapability.builder()
                .data(Map.of(
                        "1", List.of(new Capability(10L, "CapA"), new Capability(11L, "CapB")),
                        "2", List.of(new Capability(12L, "CapC"))
                ))
                .code("200")
                .message("OK")
                .date("2025-06-05")
                .build();

        List<CapabilityTechnology> capabilityTechnologies = List.of(
                new CapabilityTechnology(10L, "TechA", List.of()),
                new CapabilityTechnology(11L, "TechB", List.of()),
                new CapabilityTechnology(12L, "TechC", List.of())
        );

        when(bootcampPersistencePort.findByAllIds(bootcampIds)).thenReturn(Mono.just(bootcamps));
        when(capabilityWebClientPort.getCapabilitiesByBootcampIds(bootcampIds)).thenReturn(Mono.just(capabilities));
        when(capabilityWebClientPort.getCapabilitiesTechnologiesByIds(List.of(10L, 11L, 12L)))
                .thenReturn(Mono.just(
                        new ApiCapabilityListTechnology(null, null, null, capabilityTechnologies)
                ));

        Mono<List<BootcampListCapabilityTechnology>> resultMono =
                bootcampUseCase.findCapabilitiesByIdBootcamps(bootcampIds, "asc", 0, 10);

        StepVerifier.create(resultMono)
                .expectNextMatches(list -> {
                    if (list.size() != 2) return false;

                    BootcampListCapabilityTechnology bootcamp1 = list.get(0);
                    BootcampListCapabilityTechnology bootcamp2 = list.get(1);

                    if (!bootcamp1.getId().equals(1L)) return false;
                    if (!bootcamp1.getCapabilities().stream().map(CapabilityTechnology::getId).toList().containsAll(List.of(10L, 11L)))
                        return false;

                    if (!bootcamp2.getId().equals(2L)) return false;
                    if (!bootcamp2.getCapabilities().stream().map(CapabilityTechnology::getId).toList().contains(12L))
                        return false;

                    return true;
                })
                .verifyComplete();
    }

    @Test
    void deleteBootcamps_successfulDeletion() {
        // Arrange
        List<Long> bootcampIds = List.of(1L, 2L);

        // Bootcamps existentes
        List<Bootcamp> bootcamps = List.of(
                Bootcamp.builder().id(1L).build(),
                Bootcamp.builder().id(2L).build()
        );
        when(bootcampPersistencePort.findByAllIds(bootcampIds))
                .thenReturn(Mono.just(bootcamps));

        // Capabilities asociadas a cada bootcamp
        Map<String, List<Capability>> capabilityMap = Map.of(
                "1", List.of(new Capability(10L, "Cap1")),
                "2", List.of(new Capability(20L, "Cap2"))
        );
        when(capabilityWebClientPort.getCapabilitiesByBootcampIds(bootcampIds))
                .thenReturn(Mono.just(
                        ApiMapCapability.builder().data(capabilityMap).build()
                ));

        // Eliminación de capacidades
        when(capabilityWebClientPort.deleteBootcampCapabilities(List.of(10L)))
                .thenReturn(Mono.empty());
        when(capabilityWebClientPort.deleteBootcampCapabilities(List.of(20L)))
                .thenReturn(Mono.empty());

        // Tecnologías asociadas a cada capability
        Map<String, List<Technology>> mockTechMap = Map.of(
                "10", List.of(new Technology(100L, "Tech1")),
                "20", List.of(new Technology(200L, "Tech2"))
        );
        when(technologyWebClientPort.getTechnologiesByCapabilityIds(List.of(10L, 20L)))
                .thenReturn(Mono.just(
                        new ApiTechnologyMap("200", "OK", "2025-01-01", mockTechMap)
                ));

        // Eliminación de tecnologías
        when(technologyWebClientPort.deleteCapabilityTechnologies(List.of(100L)))
                .thenReturn(Mono.empty());
        when(technologyWebClientPort.deleteCapabilityTechnologies(List.of(200L)))
                .thenReturn(Mono.empty());

        // Transacción reactiva mockeada
        when(transactionalOperator.transactional(any(Mono.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act & Assert
        StepVerifier.create(bootcampUseCase.deleteBootcamps(bootcampIds))
                .verifyComplete();

        // Verify orden y comportamiento
        verify(bootcampPersistencePort).findByAllIds(bootcampIds);
        verify(capabilityWebClientPort).getCapabilitiesByBootcampIds(bootcampIds);
        verify(capabilityWebClientPort).deleteBootcampCapabilities(List.of(10L));
        verify(capabilityWebClientPort).deleteBootcampCapabilities(List.of(20L));
        verify(technologyWebClientPort).getTechnologiesByCapabilityIds(List.of(10L, 20L));
        verify(technologyWebClientPort).deleteCapabilityTechnologies(List.of(100L));
        verify(technologyWebClientPort).deleteCapabilityTechnologies(List.of(200L));
    }

    @Test
    void getBootcampsByIds_shouldReturnBootcamps_whenTheyExist() {
        List<Long> bootcampIds = List.of(1L, 2L);
        List<Bootcamp> bootcamps = List.of(
                Bootcamp.builder().id(1L).name("Bootcamp 1").build(),
                Bootcamp.builder().id(2L).name("Bootcamp 2").build()
        );

        when(bootcampPersistencePort.findByAllIds(bootcampIds))
                .thenReturn(Mono.just(bootcamps));

        StepVerifier.create(bootcampUseCase.getBootcampsByIds(bootcampIds))
                .expectNextMatches(b -> b.getId().equals(1L))
                .expectNextMatches(b -> b.getId().equals(2L))
                .verifyComplete();

        verify(bootcampPersistencePort).findByAllIds(bootcampIds);
    }

    @Test
    void getBootcampsByIds_shouldThrowException_whenNoBootcampsFound() {
        List<Long> bootcampIds = List.of(1L, 2L);

        when(bootcampPersistencePort.findByAllIds(bootcampIds))
                .thenReturn(Mono.just(Collections.emptyList()));

        StepVerifier.create(bootcampUseCase.getBootcampsByIds(bootcampIds))
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(BusinessException.class);
                    assertThat(((BusinessException) error).getTechnicalMessage())
                            .isEqualTo(TechnicalMessage.BOOTCAMPS_NOT_EXISTS);
                })
                .verify();

        verify(bootcampPersistencePort).findByAllIds(bootcampIds);
    }
}
