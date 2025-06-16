package com.bootcamp.project.domain.model.webclient.person.api;

import com.bootcamp.project.domain.model.webclient.person.BootcampPersonList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiBootcampPersonList {
    private String code;
    private String message;
    private String date;
    private BootcampPersonList data;
}
