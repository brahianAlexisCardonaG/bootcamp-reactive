package com.bootcamp.project.infraestructure.entrypoints.bootcamp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BootcampDto {
    private Long id;
    private String name;
    private LocalDate releaseDate;
    private Integer duration;
    private List<Long> capabilityIds;
}
