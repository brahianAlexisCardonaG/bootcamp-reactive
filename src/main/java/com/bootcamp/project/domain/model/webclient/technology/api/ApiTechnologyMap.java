package com.bootcamp.project.domain.model.webclient.technology.api;

import com.bootcamp.project.domain.model.webclient.technology.Technology;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.technology.response.TechnologyResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiTechnologyMap {
    private String code;
    private String message;
    private String date;
    private Map<String, List<Technology>> data;
}
