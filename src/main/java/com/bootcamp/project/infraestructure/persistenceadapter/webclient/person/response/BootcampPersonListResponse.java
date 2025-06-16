package com.bootcamp.project.infraestructure.persistenceadapter.webclient.person.response;

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
public class BootcampPersonListResponse {
    private Long idBootcamp;
    private String name;
    private LocalDate releaseDate;
    private Integer duration;
    private List<PersonResponse> persons;
}
