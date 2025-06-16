package com.bootcamp.project.domain.model.bootcamp;

import com.bootcamp.project.domain.model.webclient.capability.CapabilityTechnology;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BootcampListCapabilityTechnology {
    private Long id;
    private String name;
    private List<CapabilityTechnology> capabilities;
}
