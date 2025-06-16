package com.bootcamp.project.domain.usecase.bootcamp.util;

import com.bootcamp.project.domain.model.bootcamp.BootcampListCapabilityTechnology;

import java.util.Collections;
import java.util.List;

public class CapabilityPaginator {
    public static List<BootcampListCapabilityTechnology> paginateList(List<BootcampListCapabilityTechnology> sortedList,
                                                                      int skip,
                                                                      int rows) {
        if (skip >= sortedList.size()) {
            return Collections.emptyList();
        }
        int endIndex = Math.min(sortedList.size(), skip + rows);
        return sortedList.subList(skip, endIndex);
    }
}
