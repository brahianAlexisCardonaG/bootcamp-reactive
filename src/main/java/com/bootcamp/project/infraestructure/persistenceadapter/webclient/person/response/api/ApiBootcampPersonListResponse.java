package com.bootcamp.project.infraestructure.persistenceadapter.webclient.person.response.api;

import com.bootcamp.project.infraestructure.persistenceadapter.webclient.person.response.BootcampPersonListResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiBootcampPersonListResponse {
    private String code;
    private String message;
    private String date;
    private BootcampPersonListResponse data;
}
