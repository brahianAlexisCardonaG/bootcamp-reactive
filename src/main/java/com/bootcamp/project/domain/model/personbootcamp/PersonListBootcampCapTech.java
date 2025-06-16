package com.bootcamp.project.domain.model.personbootcamp;

import com.bootcamp.project.domain.model.webclient.capability.CapabilityTechnology;
import com.bootcamp.project.domain.model.webclient.person.Person;
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
public class PersonListBootcampCapTech {
    private Long id;
    private String name;
    private LocalDate releaseDate;
    private Integer duration;
    private List<Person> persons;
    private List<CapabilityTechnology> capabilities;
}
