package com.bootcamp.project.infraestructure.entrypoints.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public static final String X_MESSAGE_ID = "x-message-id";
    public static final String BOOTCAMP_ERROR = "Error on Bootcamp - [ERROR]";

    public static final String PATH_GET_CAPABILITIES_BY_IDS_BOOTCAMP
            = "/api/v1/bootcamp/find-capabilities";
    public static final String PATH_POST_BOOTCAMP_RELATE_CAPABILITIES
            = "/api/v1/bootcamp/relate-capabilities";
    public static final String PATH_DELETE_BOOTCAMPS_BY_IDS_BOOTCAMP
            = "/api/v1/bootcamp/delete";
    public static final String PATH_GET_BOOTCAMPS_BY_IDS
            = "/api/v1/bootcamp/by-ids";
    public static final String PATH_GET_BOOTCAMPS_MAX_NUMBER_PERSONS
            = "/api/v1/bootcamp/max-number-person";
}
