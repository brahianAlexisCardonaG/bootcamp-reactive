package com.bootcamp.project.infraestructure.persistenceadapter.webclient.person.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonResponse {
    private Long id;
    private String name;
    private String email;
}
