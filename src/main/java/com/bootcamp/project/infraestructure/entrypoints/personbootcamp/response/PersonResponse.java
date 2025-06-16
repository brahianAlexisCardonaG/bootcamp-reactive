package com.bootcamp.project.infraestructure.entrypoints.personbootcamp.response;

import lombok.Data;

@Data
public class PersonResponse {
    private Long id;
    private String name;
    private String email;
}
