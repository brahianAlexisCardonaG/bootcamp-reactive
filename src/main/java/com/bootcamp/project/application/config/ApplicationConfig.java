package com.bootcamp.project.application.config;

import com.bootcamp.project.domain.api.BootcampServicePort;
import com.bootcamp.project.domain.api.PersonBootcampServicePort;
import com.bootcamp.project.domain.spi.bootcamp.BootcampPersistencePort;
import com.bootcamp.project.domain.spi.bootcampmongo.BootcampMongoPersistencePort;
import com.bootcamp.project.domain.spi.webclient.CapabilityWebClientPort;
import com.bootcamp.project.domain.spi.webclient.PersonWebClientPort;
import com.bootcamp.project.domain.spi.webclient.TechnologyWebClientPort;
import com.bootcamp.project.domain.usecase.bootcamp.BootcampUseCase;
import com.bootcamp.project.domain.usecase.personbootcamp.PersonBootcampUseCase;
import com.bootcamp.project.infraestructure.persistenceadapter.bootcamp.BootcampPersistenceAdapter;
import com.bootcamp.project.infraestructure.persistenceadapter.bootcamp.mapper.BootcampEntityMapper;
import com.bootcamp.project.infraestructure.persistenceadapter.bootcamp.repository.BootcampRepository;
import com.bootcamp.project.infraestructure.persistenceadapter.mongodb.BootcampMongoDbPersistenceAdapter;
import com.bootcamp.project.infraestructure.persistenceadapter.mongodb.mapper.BootcampMongoEntityMapper;
import com.bootcamp.project.infraestructure.persistenceadapter.mongodb.repository.BootcampMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.reactive.TransactionalOperator;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final BootcampRepository bootcampRepository;
    private final BootcampEntityMapper bootcampEntityMapper;
    private final BootcampMongoRepository bootcampMongoRepository;
    private final BootcampMongoEntityMapper bootcampMongoEntityMapper;

    @Bean
    public BootcampPersistencePort bootcampPersistencePort() {
        return new BootcampPersistenceAdapter(bootcampEntityMapper,
                bootcampRepository);
    }

    @Bean
    public BootcampMongoPersistencePort bootcampMongoPersistencePort() {
        return new BootcampMongoDbPersistenceAdapter(bootcampMongoRepository,
                bootcampMongoEntityMapper);
    }


    @Bean
    public BootcampServicePort bootcampServicePort(BootcampPersistencePort capabilityPersistencePort,
                                                     CapabilityWebClientPort capabilityWebClientPort,
                                                     TransactionalOperator transactionalOperator,
                                                     TechnologyWebClientPort technologyWebClientPort,
                                                     BootcampMongoPersistencePort bootcampMongoPersistencePort
    ) {
        return new BootcampUseCase(capabilityPersistencePort,
                transactionalOperator,
                capabilityWebClientPort,
                technologyWebClientPort,
                bootcampMongoPersistencePort);
    }

    @Bean
    public PersonBootcampServicePort personBootcampServicePort(
            PersonWebClientPort personWebClientPort,
            CapabilityWebClientPort capabilityWebClientPort
    ) {
        return new PersonBootcampUseCase(personWebClientPort,capabilityWebClientPort);
    }

}
