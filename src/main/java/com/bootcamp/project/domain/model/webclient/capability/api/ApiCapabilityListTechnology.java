package com.bootcamp.project.domain.model.webclient.capability.api;

import com.bootcamp.project.domain.model.webclient.capability.CapabilityTechnology;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiCapabilityListTechnology {
    private String code;
    private String message;
    private String date;
    private List<CapabilityTechnology> data;
}
