package com.bootcamp.project.infraestructure.entrypoints.personbootcamp;

import com.bootcamp.project.infraestructure.entrypoints.bootcamp.handler.BootcampHandlerImpl;
import com.bootcamp.project.infraestructure.entrypoints.personbootcamp.handler.PersonBootcampHandlerImpl;
import com.bootcamp.project.infraestructure.entrypoints.personbootcamp.response.ApiPersonListBootcampCapTechResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
@Tag(name = "Bootcamp", description = "API Bootcamps")
@SecurityScheme(
        name = "BearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class RouterPersonBootcamp {
    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = PATH_GET_BOOTCAMPS_MAX_NUMBER_PERSONS,
                    produces = { "application/json" },
                    method = RequestMethod.GET,
                    beanClass = BootcampHandlerImpl.class,
                    beanMethod = "getBootcampsByIds",
                    operation = @Operation(
                            operationId = "getBootcampsByIds",
                            summary = "Get bootcamps with max number person",
                            tags = { "Endpoints Bootcamps" },
                            security = @SecurityRequirement(name = "BearerAuth"),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Bootcamp with max number persons found",
                                            content = @Content(schema = @Schema(implementation = ApiPersonListBootcampCapTechResponse.class))
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
    public RouterFunction<ServerResponse> routerFunctionPersonBootcamp(PersonBootcampHandlerImpl personBootcampHandler) {
        return RouterFunctions
                .route(GET(PATH_GET_BOOTCAMPS_MAX_NUMBER_PERSONS), personBootcampHandler::getPersonsByBootcampsByIdMaxNumberPerson);
    }
}
