package com.bootcamp.project.infraestructure.persistenceadapter.mongodb.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDate;

@Document(collection = "bootcamp")
@Data
public class BootcampMongoEntity {
    @Id
    private Long id;
    @Column("id_bootcamp")
    private Long idBootcamp;
    private String name;
    @Column("release_date")
    private LocalDate releaseDate;
    private Integer duration;
    private Integer numberCapabilities;
    private Integer numberTechnologies;
    private Integer numberPersons;
}
