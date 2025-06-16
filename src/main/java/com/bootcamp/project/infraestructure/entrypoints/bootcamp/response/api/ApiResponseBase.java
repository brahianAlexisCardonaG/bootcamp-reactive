package com.bootcamp.project.infraestructure.entrypoints.bootcamp.response.api;

import com.bootcamp.project.infraestructure.entrypoints.bootcamp.dto.BootcampDto;
import com.bootcamp.project.infraestructure.entrypoints.util.error.ErrorDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class ApiResponseBase {
    private String code;
    private String message;
    private String date;
    private List<BootcampDto> data;
    private List<ErrorDto> errors;
}
