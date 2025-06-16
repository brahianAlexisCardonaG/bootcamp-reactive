package com.bootcamp.project.domain.model.webclient.capability.api;

import com.bootcamp.project.domain.model.webclient.capability.Capability;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class ApiMapCapability {
    private String code;
    private String message;
    private String date;
    private Map<String, List<Capability>> data;
}
