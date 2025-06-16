package com.bootcamp.project.domain.model.webclient.capability;

import com.bootcamp.project.domain.model.webclient.technology.Technology;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CapabilityTechnology {
    private Long id;
    private String name;
    private List<Technology> technologies;
}
