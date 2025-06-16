package com.bootcamp.project.infraestructure.entrypoints.bootcamp.response;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BootcampResponse {
    private Long id;
    private String name;
    private LocalDate releaseDate;
    private Integer duration;
}
