package com.bootcamp.project.domain.model.webclient.capability.api;

import com.bootcamp.project.domain.model.webclient.capability.Capability;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiCapabilityList {
    private String code;
    private String message;
    private String date;
    private List<Capability> data;
}
