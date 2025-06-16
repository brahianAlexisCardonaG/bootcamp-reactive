package com.bootcamp.project.infraestructure.persistenceadapter.webclient.capability.response;

import com.bootcamp.project.infraestructure.persistenceadapter.webclient.technology.response.TechnologyResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CapabilityListTechnologyResponse {
    private Long id;
    private String name;
    private List<TechnologyResponse> technologies;
}
