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
        BootcampMongo bootcamp = BootcampMongo.builder()
                .id(1L)
                .idBootcamp(101L)
                .name("Bootcamp Test")
                .releaseDate(LocalDate.of(2025, 6, 14))
                .duration(30)
                .numberCapabilities(2)
                .numberTechnologies(1)
                .numberPersons(0)
                .build();

        BootcampMongoEntity entity = new BootcampMongoEntity();
        entity.setId(1L);
        entity.setIdBootcamp(101L);
        entity.setName("Bootcamp Test");
        entity.setReleaseDate(LocalDate.of(2025, 6, 14));
        entity.setDuration(30);
        entity.setNumberCapabilities(2);
        entity.setNumberTechnologies(1);
        entity.setNumberPersons(0);

        when(mapper.toEntity(bootcamp)).thenReturn(entity);
        when(repository.saveAll(anyList())).thenReturn(Flux.just(entity));

        Mono<Void> result = adapter.saveAll(List.of(bootcamp));

        StepVerifier.create(result)
                .verifyComplete();

        verify(repository, times(1)).saveAll(anyList());
    }

    @Test
    void findByIdBootcamp_shouldReturnMatchingBootcampMongoList() {

        Long id = 101L;
        BootcampMongoEntity entity = new BootcampMongoEntity();
        entity.setIdBootcamp(id);

        BootcampMongo model = BootcampMongo.builder()
                .idBootcamp(id)
                .build();

        when(repository.findByIdBootcampIn(List.of(id))).thenReturn(Flux.just(entity));
        when(mapper.toModel(entity)).thenReturn(model);

        StepVerifier.create(adapter.findByIdBootcamp(List.of(id)))
                .expectNextMatches(list -> list.size() == 1 && list.get(0).getIdBootcamp().equals(id))
                .verifyComplete();
    }

    @Test
    void delete_shouldCallRepositoryDeleteByIdBootcampIn() {
        List<Long> ids = List.of(101L);
        when(repository.deleteByIdBootcampIn(ids)).thenReturn(Mono.empty());

        Mono<Void> result = adapter.delete(ids);
        StepVerifier.create(result).verifyComplete();

        verify(repository, times(1)).deleteByIdBootcampIn(ids);
    }

}
