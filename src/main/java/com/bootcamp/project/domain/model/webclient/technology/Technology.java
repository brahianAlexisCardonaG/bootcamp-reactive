package com.bootcamp.project.domain.model.webclient.technology;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Technology {
    private Long id;
    private String name;
}
