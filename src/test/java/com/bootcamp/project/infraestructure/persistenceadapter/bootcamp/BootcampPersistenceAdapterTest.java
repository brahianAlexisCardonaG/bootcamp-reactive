package com.bootcamp.project.infraestructure.persistenceadapter.bootcamp;

import com.bootcamp.project.domain.model.bootcamp.Bootcamp;
import com.bootcamp.project.infraestructure.persistenceadapter.bootcamp.mapper.BootcampEntityMapper;
import com.bootcamp.project.infraestructure.persistenceadapter.bootcamp.repository.BootcampRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BootcampPersistenceAdapterTest {
    @Mock
    private BootcampEntityMapper bootcampEntityMapper;

    @Mock
    private BootcampRepository bootcampRepository;

    @InjectMocks
    private BootcampPersistenceAdapter adapter;

    private Bootcamp sampleDomain;
    private com.bootcamp.project.infraestructure.persistenceadapter.bootcamp.entity.BootcampEntity sampleEntity;

    @BeforeEach
    void setUp() {
        // Crear instancias de ejemplo para dominio y entidad
        sampleDomain = Bootcamp.builder()
                .id(1L)
                .name("Test Bootcamp")
                .releaseDate(LocalDate.of(2025, 1, 1))
                .duration(6)
                .capabilityIds(List.of(10L, 20L))
                .build();

        sampleEntity = com.bootcamp.project.infraestructure.persistenceadapter.bootcamp.entity.BootcampEntity.builder()
                .id(1L)
                .name("Test Bootcamp")
                .releaseDate(LocalDate.of(2025, 1, 1))
                .duration(6)
                .build();
    }

    @Test
    void save_whenCalled_persistsAndMapsBack() {
        Flux<Bootcamp> domainFlux = Flux.just(sampleDomain);

        when(bootcampEntityMapper.toEntity(sampleDomain)).thenReturn(sampleEntity);

        when(bootcampRepository.saveAll(ArgumentMatchers.anyList()))
                .thenReturn(Flux.just(sampleEntity));

        when(bootcampEntityMapper.toModel(sampleEntity)).thenReturn(sampleDomain);

        // Ejecutar el método save
        Flux<Bootcamp> resultFlux = adapter.save(domainFlux);

        // Verificar que el resultado emite el Bootcamp mapeado
        StepVerifier.create(resultFlux)
                .expectNextMatches(b ->
                        b.getId().equals(sampleDomain.getId()) &&
                                b.getName().equals(sampleDomain.getName()) &&
                                b.getReleaseDate().equals(sampleDomain.getReleaseDate()) &&
                                b.getDuration().equals(sampleDomain.getDuration()) &&
                                b.getCapabilityIds().equals(sampleDomain.getCapabilityIds())
                )
                .verifyComplete();

        // Verificar interacciones con mocks
        verify(bootcampEntityMapper, times(1)).toEntity(sampleDomain);
        verify(bootcampRepository, times(1)).saveAll(ArgumentMatchers.anyList());
        verify(bootcampEntityMapper, times(1)).toModel(sampleEntity);
    }

    @Test
    void findByName_whenExists_returnsTrue() {
        String name = "Test Bootcamp";

        // Stub: repository.findByName(name) devuelve Mono<entity>
        when(bootcampRepository.findByName(name)).thenReturn(Mono.just(sampleEntity));

        // Stub: mapper.toModel(entity) devuelve un dominio
        when(bootcampEntityMapper.toModel(sampleEntity)).thenReturn(sampleDomain);

        // Ejecutar el método findByName
        Mono<Boolean> resultMono = adapter.findByName(name);

        // Verificar que emite true
        StepVerifier.create(resultMono)
                .expectNext(true)
                .verifyComplete();

        verify(bootcampRepository, times(1)).findByName(name);
        verify(bootcampEntityMapper, times(1)).toModel(sampleEntity);
    }

    @Test
    void findByName_whenNotExists_returnsFalse() {
        String name = "Nonexistent";

        when(bootcampRepository.findByName(name)).thenReturn(Mono.empty());

        Mono<Boolean> resultMono = adapter.findByName(name);

        StepVerifier.create(resultMono)
                .expectNext(false)
                .verifyComplete();

        verify(bootcampRepository, times(1)).findByName(name);
        verify(bootcampEntityMapper, never()).toModel(any());
    }

    @Test
    void findByAllIds_whenCalled_returnsListOfBootcamps() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        // Crear entidades de ejemplo para cada id
        com.bootcamp.project.infraestructure.persistenceadapter.bootcamp.entity.BootcampEntity entity1 =
                com.bootcamp.project.infraestructure.persistenceadapter.bootcamp.entity.BootcampEntity.builder()
                        .id(1L).name("First").releaseDate(LocalDate.of(2025, 2, 1)).duration(4).build();
        com.bootcamp.project.infraestructure.persistenceadapter.bootcamp.entity.BootcampEntity entity2 =
                com.bootcamp.project.infraestructure.persistenceadapter.bootcamp.entity.BootcampEntity.builder()
                        .id(2L).name("Second").releaseDate(LocalDate.of(2025, 3, 1)).duration(8).build();
        com.bootcamp.project.infraestructure.persistenceadapter.bootcamp.entity.BootcampEntity entity3 =
                com.bootcamp.project.infraestructure.persistenceadapter.bootcamp.entity.BootcampEntity.builder()
                        .id(3L).name("Third").releaseDate(LocalDate.of(2025, 4, 1)).duration(12).build();

        when(bootcampRepository.findAllById(ids))
                .thenReturn(Flux.just(entity1, entity2, entity3));

        Bootcamp model1 = Bootcamp.builder()
                .id(1L).name("First").releaseDate(LocalDate.of(2025, 2, 1)).duration(4).capabilityIds(List.of()).build();
        Bootcamp model2 = Bootcamp.builder()
                .id(2L).name("Second").releaseDate(LocalDate.of(2025, 3, 1)).duration(8).capabilityIds(List.of()).build();
        Bootcamp model3 = Bootcamp.builder()
                .id(3L).name("Third").releaseDate(LocalDate.of(2025, 4, 1)).duration(12).capabilityIds(List.of()).build();

        when(bootcampEntityMapper.toModel(entity1)).thenReturn(model1);
        when(bootcampEntityMapper.toModel(entity2)).thenReturn(model2);
        when(bootcampEntityMapper.toModel(entity3)).thenReturn(model3);

        Mono<List<Bootcamp>> resultMono = adapter.findByAllIds(ids);

        StepVerifier.create(resultMono)
                .expectNextMatches(list ->
                        list.size() == 3 &&
                                list.containsAll(Arrays.asList(model1, model2, model3))
                )
                .verifyComplete();

        verify(bootcampRepository, times(1)).findAllById(ids);
        verify(bootcampEntityMapper, times(1)).toModel(entity1);
        verify(bootcampEntityMapper, times(1)).toModel(entity2);
        verify(bootcampEntityMapper, times(1)).toModel(entity3);
    }
}
