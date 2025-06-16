package com.bootcamp.project.infraestructure.entrypoints.bootcamp.response.api;

import com.bootcamp.project.infraestructure.entrypoints.bootcamp.response.BootcampListCapabilityTechnologyResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ApiBootcampCapabilityTechnologyResponseList {
    private String code;
    private String message;
    private String date;
    List<BootcampListCapabilityTechnologyResponse> data;
}
