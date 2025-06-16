package com.bootcamp.project.infraestructure.persistenceadapter.webclient.capability.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CapabilityResponse {
    private Long id;
    private String name;
}
