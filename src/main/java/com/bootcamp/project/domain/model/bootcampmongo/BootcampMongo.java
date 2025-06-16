package com.bootcamp.project.domain.model.bootcampmongo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BootcampMongo {
    private Long id;
    private Long idBootcamp;
    private String name;
    private LocalDate releaseDate;
    private Integer duration;
    private Integer numberCapabilities;
    private Integer numberTechnologies;
    private Integer numberPersons;
}
