package com.bootcamp.project.infraestructure.entrypoints.bootcamp.response.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiBootcampDeleteResponse {
    private String code;
    private String message;
    private String date;
}
