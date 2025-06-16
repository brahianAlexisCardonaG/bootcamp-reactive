package com.bootcamp.project.infraestructure.persistenceadapter.webclient.technology.response.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiTechnologyMessageResponse {
    private String code;
    private String message;
    private String date;
}
