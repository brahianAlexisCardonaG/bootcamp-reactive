package com.bootcamp.project.infraestructure.persistenceadapter.webclient.technology.response.api;

import com.bootcamp.project.infraestructure.persistenceadapter.webclient.technology.response.TechnologyResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiTechnologyMapResponse {
    private String code;
    private String message;
    private String date;
    private Map<String, List<TechnologyResponse>> data;
}
