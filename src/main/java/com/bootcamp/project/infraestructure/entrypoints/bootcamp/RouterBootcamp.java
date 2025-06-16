package com.bootcamp.project.infraestructure.entrypoints.bootcamp;

import com.bootcamp.project.infraestructure.entrypoints.bootcamp.dto.BootcampDto;
import com.bootcamp.project.infraestructure.entrypoints.bootcamp.handler.BootcampHandlerImpl;
import com.bootcamp.project.infraestructure.entrypoints.bootcamp.response.api.ApiBootcampDeleteResponse;
import com.bootcamp.project.infraestructure.entrypoints.bootcamp.response.api.ApiResponseBase;
import com.bootcamp.project.infraestructure.entrypoints.bootcamp.response.api.ApiBootcampCapabilityTechnologyResponseList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.bootcamp.project.infraestructure.entrypoints.util.Constants.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
@Tag(name = "Bootcamp", description = "API Bootcamps")
@SecurityScheme(
        name = "BearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class RouterBootcamp {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = PATH_POST_BOOTCAMP_RELATE_CAPABILITIES,
                    produces = { "application/json" },
                    method = RequestMethod.POST,
                    beanClass = BootcampHandlerImpl.class,
                    beanMethod = "createBootcampRelateCapabilities",
                    operation = @Operation(
                            operationId = "createBootcampRelateCapabilities",
                            summary = "Create or update a bootcamp associating capabilities",
                            tags = { "Endpoints Bootcamps" },
                            security = @SecurityRequirement(name = "BearerAuth"),
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(array
                                            = @ArraySchema(schema = @Schema(implementation = BootcampDto.class)))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Bootcamp created/updated with capabilities successfully",
                                            content = @Content(schema
                                                    = @Schema(implementation = ApiBootcampCapabilityTechnologyResponseList.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Validation error"
                                    ),
                                    @ApiResponse(responseCode = "401", description = "Unauthorized")

                            }
                    )
            ),
            @RouterOperation(
                    path = PATH_GET_CAPABILITIES_BY_IDS_BOOTCAMP,
                    produces = { "application/json" },
                    method = RequestMethod.GET,
                    beanClass = BootcampHandlerImpl.class,
                    beanMethod = "getCapabilitiesByBootcampsIds",
                    operation = @Operation(
                            operationId = "getCapabilitiesByBootcampsIds",
                            summary = "Obtain capabilities by Bootcamp IDs",
                            tags = { "Endpoints Bootcamps" },
                            security = @SecurityRequirement(name = "BearerAuth"),
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.QUERY,
                                            name = "bootcampIds",
                                            description = "List of bootcamp IDs separated by commas (e.g., 1,2,3)",
                                            example = "1,2,3",
                                            required = true
                                    ),
                                    @Parameter(
                                            in = ParameterIn.QUERY,
                                            name = "order",
                                            description = "Sort order (asc, desc or cap)",
                                            example = "asc",
                                            required = false
                                    ),
                                    @Parameter(
                                            in = ParameterIn.QUERY,
                                            name = "rows",
                                            description = "Number of rows to return",
                                            example = "5",
                                            required = false
                                    ),
                                    @Parameter(
                                            in = ParameterIn.QUERY,
                                            name = "skip",
                                            description = "Number of items to skip",
                                            example = "0",
                                            required = false
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Capabilities found",
                                            content = @Content(schema = @Schema(implementation = ApiBootcampCapabilityTechnologyResponseList.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Invalid query parameters"
                                    ),
                                    @ApiResponse(responseCode = "401", description = "Unauthorized")
                            }
                    )
            ),
            @RouterOperation(
                    path = PATH_DELETE_BOOTCAMPS_BY_IDS_BOOTCAMP,
                    produces = { "application/json" },
                    method = RequestMethod.DELETE,
                    beanClass = BootcampHandlerImpl.class,
                    beanMethod = "deleteBootcampsByIds",
                    operation = @Operation(
                            operationId = "deleteBootcampsByIds",
                            summary = "Delete technologies, capabilities and bootcamps by bootcamp Ids",
                            tags = { "Endpoints Bootcamps" },
                            security = @SecurityRequirement(name = "BearerAuth"),
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.QUERY,
                                            name = "bootcampIds",
                                            description = "List of bootcamp IDs separated by commas (e.g., 1,2,3)",
                                            example = "1,2,3",
                                            required = true
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Bootcamps deleted",
                                            content = @Content(schema = @Schema(implementation = ApiBootcampDeleteResponse.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Invalid query parameters"
                                    ),
                                    @ApiResponse(responseCode = "401", description = "Unauthorized")
                            }
                    )
            ),
            @RouterOperation(
                    path = PATH_GET_BOOTCAMPS_BY_IDS,
                    produces = { "application/json" },
                    method = RequestMethod.GET,
                    beanClass = BootcampHandlerImpl.class,
                    beanMethod = "getBootcampsByIds",
                    operation = @Operation(
                            operationId = "getBootcampsByIds",
                            summary = "Get bootcamps by bootcamp Ids",
                            tags = { "Endpoints webclients" },
                            security = @SecurityRequirement(name = "BearerAuth"),
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.QUERY,
                                            name = "bootcampIds",
                                            description = "List of bootcamp IDs separated by commas (e.g., 1,2,3)",
                                            example = "1,2,3",
                                            required = true
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Bootcamps found",
                                            content = @Content(schema = @Schema(implementation = ApiResponseBase.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Invalid query parameters"
                                    ),
                                    @ApiResponse(responseCode = "401", description = "Unauthorized")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunctionBootcamp(BootcampHandlerImpl bootcampHandlerImpl) {
        return RouterFunctions
                .route(POST(PATH_POST_BOOTCAMP_RELATE_CAPABILITIES), bootcampHandlerImpl::createBootcampRelateCapabilities)
                .andRoute(GET(PATH_GET_CAPABILITIES_BY_IDS_BOOTCAMP), bootcampHandlerImpl::getCapabilitiesByBootcampsIds)
                .andRoute(DELETE(PATH_DELETE_BOOTCAMPS_BY_IDS_BOOTCAMP), bootcampHandlerImpl::deleteBootcampsByIds)
                .andRoute(GET(PATH_GET_BOOTCAMPS_BY_IDS), bootcampHandlerImpl::getBootcampsByIds);

    }
}