package com.bootcamp.project.infraestructure.entrypoints.bootcamp.response;

import com.bootcamp.project.infraestructure.persistenceadapter.webclient.capability.response.CapabilityListTechnologyResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BootcampListCapabilityTechnologyResponse {
    private Long id;
    private String name;
    private List<CapabilityListTechnologyResponse> capabilities;
}
