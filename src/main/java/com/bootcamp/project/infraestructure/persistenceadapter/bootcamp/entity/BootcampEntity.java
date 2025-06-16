package com.bootcamp.project.infraestructure.persistenceadapter.bootcamp.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table(name = "bootcamp")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BootcampEntity {
    @Id
    private Long id;
    private String name;
    @Column("release_date")
    private LocalDate releaseDate;
    private Integer duration;
}
