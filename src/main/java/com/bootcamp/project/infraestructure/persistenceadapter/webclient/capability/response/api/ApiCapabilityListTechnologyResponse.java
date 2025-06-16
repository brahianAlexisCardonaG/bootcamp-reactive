package com.bootcamp.project.infraestructure.persistenceadapter.webclient.capability.response.api;

import com.bootcamp.project.infraestructure.persistenceadapter.webclient.capability.response.CapabilityListTechnologyResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiCapabilityListTechnologyResponse {
    private String code;
    private String message;
    private String date;
    private List<CapabilityListTechnologyResponse> data;
}
