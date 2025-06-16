package com.bootcamp.project.domain.model.webclient.capability.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiCapabilityMessage {
    private String code;
    private String message;
    private String date;
}
