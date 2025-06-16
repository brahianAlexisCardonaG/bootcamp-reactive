package com.bootcamp.project.domain.model.webclient.person;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private Long id;
    private String name;
    private String email;
}
