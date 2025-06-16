package com.bootcamp.project.infraestructure.entrypoints.personbootcamp.response;

import com.bootcamp.project.infraestructure.persistenceadapter.webclient.capability.response.CapabilityListTechnologyResponse;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PersonListBootcampCapTechResponse {
    private Long id;
    private String name;
    private LocalDate releaseDate;
    private Integer duration;
    private List<PersonResponse> persons;
    private List<CapabilityListTechnologyResponse> capabilities;
}
