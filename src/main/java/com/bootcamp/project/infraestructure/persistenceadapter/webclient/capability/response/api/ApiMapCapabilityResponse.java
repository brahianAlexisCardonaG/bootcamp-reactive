package com.bootcamp.project.infraestructure.persistenceadapter.webclient.capability.response.api;

import com.bootcamp.project.infraestructure.persistenceadapter.webclient.capability.response.CapabilityResponse;
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
public class ApiMapCapabilityResponse {
    private String code;
    private String message;
    private String date;
    private Map<String, List<CapabilityResponse>> data;
}
