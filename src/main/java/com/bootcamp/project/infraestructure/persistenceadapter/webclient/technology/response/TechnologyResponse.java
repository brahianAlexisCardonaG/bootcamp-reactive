package com.bootcamp.project.infraestructure.persistenceadapter.webclient.technology.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TechnologyResponse {
    private Long id;
    private String name;
}
