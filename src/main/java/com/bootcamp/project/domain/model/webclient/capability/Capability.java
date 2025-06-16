package com.bootcamp.project.domain.model.webclient.capability;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Capability {
    private Long id;
    private String name;
}
