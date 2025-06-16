package com.bootcamp.project.domain.model.webclient.person;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BootcampPersonList {
    private Long idBootcamp;
    private String name;
    private LocalDate releaseDate;
    private Integer duration;
    private List<Person> persons;
}
