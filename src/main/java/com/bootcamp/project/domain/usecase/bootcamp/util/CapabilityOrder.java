package com.bootcamp.project.domain.usecase.bootcamp.util;

import com.bootcamp.project.domain.model.bootcamp.BootcampListCapabilityTechnology;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CapabilityOrder {

    public static List<BootcampListCapabilityTechnology> sortList(
            List<BootcampListCapabilityTechnology> unsortedList,
            String order) {

        Comparator<BootcampListCapabilityTechnology> comparator;

        if (order != null && order.equalsIgnoreCase("cap")) {
            // Orden descendente por cantidad de tecnologías
            comparator = Comparator.comparingInt(
                    (BootcampListCapabilityTechnology c) -> c.getCapabilities().size()
            ).reversed();
        }
        else if (order != null && order.equalsIgnoreCase("desc")) {
            // Orden descendente alfabéticamente por nombre de la capacidad
            comparator = Comparator.comparing(
                    BootcampListCapabilityTechnology::getName,
                    Comparator.reverseOrder()
            );
        }
        else {
            // Orden ascendente (por defecto) alfabéticamente por nombre de la capacidad
            comparator = Comparator.comparing(
                    BootcampListCapabilityTechnology::getName
            );
        }

        return unsortedList.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

}
