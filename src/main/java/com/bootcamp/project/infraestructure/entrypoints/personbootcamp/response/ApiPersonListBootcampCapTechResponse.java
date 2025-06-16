package com.bootcamp.project.infraestructure.entrypoints.personbootcamp.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiPersonListBootcampCapTechResponse {
    private String code;
    private String message;
    private String date;
    private PersonListBootcampCapTechResponse data;
}
