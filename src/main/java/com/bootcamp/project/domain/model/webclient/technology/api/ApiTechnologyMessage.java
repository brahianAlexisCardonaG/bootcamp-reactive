package com.bootcamp.project.domain.model.webclient.technology.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiTechnologyMessage {
    private String code;
    private String message;
    private String date;
}
