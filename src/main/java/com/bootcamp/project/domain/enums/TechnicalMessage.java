package com.bootcamp.project.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TechnicalMessage {
    INTERNAL_ERROR("500", "Something went wrong, please try again", ""),
    INTERNAL_ERROR_IN_ADAPTERS("PRC501", "Something went wrong in adapters, please try again", ""),
    INVALID_REQUEST("400", "Bad Request, please verify data", ""),
    INVALID_PARAMETERS(INVALID_REQUEST.getCode(), "Bad Parameters, please verify data", ""),
    CAPABILITY_ALREADY_EXISTS("400", "The Capability already found register.", ""),
    NAME_TOO_LONG("404-1", "The name must not exceed the 50 characters", ""),
    BOOTCAMP_CAPABILITY_RELATION("201", "Successfully Created Bootcamps and Successfully Associated Capabilities", ""),
    BOOTCAMP_NOT_EXISTS("400", "The capabilityId not found.", ""),
    CAPABILITIES_NOT_EXISTS("400", "Some of the capabilities are not registered.", ""),
    DUPLICATE_NAMES_BOOTCAMP("404-7", "The names of the Bootcamps cannot be the same", ""),
    BOOTCAMP_CAPABILITY_FOUND("200","Capabilities by Bootcamps found",""),
    BOOTCAMPS_NOT_EXISTS("400", "Some of the Bootcamps are not registered.", ""),
    BOOTCAMPS_DELETE_SUCCESSFULLY("200"," The bootcamps was delete successfully." ,"" ),
    BOOTCAMP_PERSON_MAX_NUMBER_PERSONS("200","Bootcamp with max number of person found","");

    private final String code;
    private final String message;
    private final String param;

}
