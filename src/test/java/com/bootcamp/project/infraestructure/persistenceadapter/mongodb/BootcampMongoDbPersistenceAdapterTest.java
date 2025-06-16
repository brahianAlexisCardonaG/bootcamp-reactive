package com.bootcamp.project.infraestructure.persistenceadapter.mongodb;


import com.bootcamp.project.domain.model.bootcampmongo.BootcampMongo;
import com.bootcamp.project.infraestructure.persistenceadapter.mongodb.entity.BootcampMongoEntity;
import com.bootcamp.project.infraestructure.persistenceadapter.mongodb.mapper.BootcampMongoEntityMapper;
import com.bootcamp.project.infraestructure.persistenceadapter.mongodb.repository.BootcampMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BootcampMongoDbPersistenceAdapterTest {
    @Mock
    private BootcampMongoRepository repository;

    @Mock
    private BootcampMongoEntityMapper mapper;

    @InjectMocks
    private BootcampMongoDbPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        repository = mock(BootcampMongoRepository.class);
        mapper = mock(BootcampMongoEntityMapper.class);
        adapter = new BootcampMongoDbPersistenceAdapter(repository, mapper);
    }

    @Test
    void saveAll_shouldSaveBootcampMongoEntities() {
        // Arrange
        BootcampMongo bootcamp1 = BootcampMongo.builder()
                .id(1L)
                .idBootcamp(101L)
                .name("Bootcamp Test")
                .releaseDate(LocalDate.of(2025, 6, 14))
                .duration(30)
                .numberCapabilities(2)
                .numberTechnologies(1)
                .numberPersons(0)
                .build();

        BootcampMongoEntity entity1 = new BootcampMongoEntity();
        entity1.setId(1L);
        entity1.setIdBootcamp(101L);
        entity1.setName("Bootcamp Test");
        entity1.setReleaseDate(LocalDate.of(2025, 6, 14));
        entity1.setDuration(30);
        entity1.setNumberCapabilities(2);
        entity1.setNumberTechnologies(1);
        entity1.setNumberPersons(0);

        when(mapper.toEntity(bootcamp1)).thenReturn(entity1);
        when(repository.saveAll(anyList())).thenReturn(Flux.fromIterable(List.of(entity1)));

        // Act
        Mono<Void> result = adapter.saveAll(Flux.just(bootcamp1));

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        ArgumentCaptor<List<BootcampMongoEntity>> captor = ArgumentCaptor.forClass(List.class);
        verify(repository).saveAll(captor.capture());

        List<BootcampMongoEntity> savedEntities = captor.getValue();
        assertThat(savedEntities).hasSize(1);
        assertThat(savedEntities.get(0).getIdBootcamp()).isEqualTo(101L);
    }
}
