package com.bootcamp.project.infraestructure.persistenceadapter.webclient.capability.response.api;

import com.bootcamp.project.infraestructure.persistenceadapter.webclient.capability.response.CapabilityResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiCapabilityListResponse {
    private String code;
    private String message;
    private String date;
    private List<CapabilityResponse> data;
}
