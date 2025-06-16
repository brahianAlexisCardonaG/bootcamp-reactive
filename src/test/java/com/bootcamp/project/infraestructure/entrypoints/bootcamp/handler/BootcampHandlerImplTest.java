package com.bootcamp.project.infraestructure.entrypoints.bootcamp.handler;

import com.bootcamp.project.domain.api.BootcampServicePort;
import com.bootcamp.project.domain.model.bootcamp.Bootcamp;
import com.bootcamp.project.domain.model.bootcamp.BootcampListCapabilityTechnology;
import com.bootcamp.project.infraestructure.entrypoints.bootcamp.dto.BootcampDto;
import com.bootcamp.project.infraestructure.entrypoints.bootcamp.mapper.BootcampMapper;
import com.bootcamp.project.infraestructure.entrypoints.util.Constants;
import com.bootcamp.project.infraestructure.entrypoints.util.error.ApplyErrorHandler;
import com.bootcamp.project.infraestructure.entrypoints.bootcamp.validate.ValidateRequestSave;
import com.bootcamp.project.infraestructure.entrypoints.bootcamp.mapper.BootcampMapperResponse;
import com.bootcamp.project.infraestructure.entrypoints.bootcamp.response.api.ApiBootcampCapabilityTechnologyResponseList;
import com.bootcamp.project.infraestructure.entrypoints.bootcamp.response.BootcampListCapabilityTechnologyResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;

import static com.bootcamp.project.infraestructure.entrypoints.util.Constants.PATH_POST_BOOTCAMP_RELATE_CAPABILITIES;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BootcampHandlerImplTest {

    @InjectMocks
    private BootcampHandlerImpl bootcampHandlerImpl;

    @Mock
    private ValidateRequestSave validateRequestSave;

    @Mock
    private BootcampMapper bootcampMapper;

    @Mock
    private BootcampServicePort bootcampServicePort;

    @Mock
    private ApplyErrorHandler applyErrorHandler;

    @Mock
    private BootcampMapperResponse bootcampMapperResponse;

    private WebTestClient webTestClient;

    @BeforeEach
    public void setup() {
        // Configuramos el RouterFunction para incluir ambas rutas: POST y GET.
        RouterFunction<ServerResponse> routerFunction = RouterFunctions
                .route(RequestPredicates.POST(Constants.PATH_POST_BOOTCAMP_RELATE_CAPABILITIES)
                                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        bootcampHandlerImpl::createBootcampRelateCapabilities)
                .andRoute(RequestPredicates.GET(Constants.PATH_GET_CAPABILITIES_BY_IDS_BOOTCAMP)
                                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        bootcampHandlerImpl::getCapabilitiesByBootcampsIds);

        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    public void testCreateBootcampRelateCapabilities_Success() {
        // Arrange
        BootcampDto bootcampDto = new BootcampDto(); // Puede inicializarse con valores de ser necesario
        Bootcamp bootcamp = new Bootcamp();
        BootcampListCapabilityTechnology capabilityEntity = new BootcampListCapabilityTechnology();
        BootcampListCapabilityTechnologyResponse capabilityResponse =
                new BootcampListCapabilityTechnologyResponse();

        // Simulamos que validateRequestSave retorna un BootcampDto.
        when(validateRequestSave.validateAndMapRequest(any(ServerRequest.class)))
                .thenReturn(Flux.just(bootcampDto));

        // Se mapea el DTO a la entidad de dominio.
        when(bootcampMapper.toBootcamp(any(BootcampDto.class))).thenReturn(bootcamp);

        // Importante: en lugar de usar thenReturn, usamos thenAnswer para obligar a consumir el flux,
        // lo que disparará los operadores previos (incluyendo el map que debe invocar bootcampMapper.toBootcamp).
        when(bootcampServicePort.saveBootcampCapability(any(Flux.class)))
                .thenAnswer(invocation -> {
                    Flux<Bootcamp> flux = invocation.getArgument(0);
                    // Colectamos el flux y devolvemos un Mono con la lista esperada.
                    return flux.collectList()
                            .map(list -> {
                                // En este punto, se debe haber llamado a bootcampMapper.toBootcamp.
                                return List.of(capabilityEntity);
                            });
                });

        // Se transforma la entidad guardada a la respuesta esperada.
        when(bootcampMapperResponse
                .toBootcampListCapabilityTechnologyResponse(any(BootcampListCapabilityTechnology.class)))
                .thenReturn(capabilityResponse);

        // Configuramos ApplyErrorHandler para devolver el mismo Mono sin modificar el flujo.
        when(applyErrorHandler.applyErrorHandling(any(Mono.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act & Assert
        webTestClient.post()
                .uri(PATH_POST_BOOTCAMP_RELATE_CAPABILITIES)
                .contentType(MediaType.APPLICATION_JSON)
                // Se envía un JSON simple (por ejemplo, un arreglo con un objeto vacío); ajústalo según el formato real de BootcampDto.
                .bodyValue("[{}]")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ApiBootcampCapabilityTechnologyResponseList.class)
                .consumeWith(response -> {
                    ApiBootcampCapabilityTechnologyResponseList responseBody = response.getResponseBody();
                    // Aquí puedes agregar más aserciones sobre el cuerpo, por ejemplo:
                    // assertEquals(TechnicalMessage.BOOTCAMP_CAPABILITY_RELATION.getCode(), responseBody.getCode());
                    assert responseBody != null;
                });

        // Verificamos que se invoquen los métodos según lo esperado:
        verify(validateRequestSave, times(1)).validateAndMapRequest(any(ServerRequest.class));
        verify(bootcampMapper, times(1)).toBootcamp(any(BootcampDto.class));
        verify(bootcampServicePort, times(1)).saveBootcampCapability(any(Flux.class));
        verify(applyErrorHandler, times(1)).applyErrorHandling(any(Mono.class));
    }

    @Test
    public void testGetCapabilitiesByBootcampsIds_Success() {
        // Arrange
        // Se espera que el queryParam "bootcampIds" se envíe como string separada por comas.
        // Por ejemplo: "1,2,3" se convertirá en List.of(1L, 2L, 3L)
        List<Long> expectedIds = List.of(1L, 2L, 3L);
        String order = "asc";
        int skip = 0;
        int rows = 4;

        // Simulamos que el service retorna una lista con una entidad de tipo BootcampListCapabilityTechnology.
        BootcampListCapabilityTechnology capabilityEntity = new BootcampListCapabilityTechnology();
        when(bootcampServicePort
                .findCapabilitiesByIdBootcamps(expectedIds, order, skip, rows))
                .thenReturn(Mono.just(List.of(capabilityEntity)));

        // Se simula el mapeo de la entidad a la respuesta (DTO).
        BootcampListCapabilityTechnologyResponse capabilityResponse =
                new BootcampListCapabilityTechnologyResponse();
        when(bootcampMapperResponse
                .toBootcampListCapabilityTechnologyResponse(any(BootcampListCapabilityTechnology.class)))
                .thenReturn(capabilityResponse);

        // Configuramos el ApplyErrorHandler para que devuelva el mismo flujo sin modificarlo.
        when(applyErrorHandler.applyErrorHandling(any(Mono.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act & Assert
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(Constants.PATH_GET_CAPABILITIES_BY_IDS_BOOTCAMP)
                        .queryParam("bootcampIds", "1,2,3")
                        .queryParam("order", order)
                        .queryParam("rows", String.valueOf(rows))
                        .queryParam("skip", String.valueOf(skip))
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ApiBootcampCapabilityTechnologyResponseList.class)
                .consumeWith(response -> {
                    ApiBootcampCapabilityTechnologyResponseList responseBody = response.getResponseBody();
                    // Se pueden agregar aserciones adicionales, por ejemplo:
                    // assertEquals(TechnicalMessage.BOOTCAMP_CAPABILITY_FOUND.getCode(), responseBody.getCode());
                    assert responseBody != null;
                });

        // Verificamos que el service fue invocado con los parámetros esperados.
        verify(bootcampServicePort, times(1))
                .findCapabilitiesByIdBootcamps(expectedIds, order, skip, rows);
        verify(applyErrorHandler, times(1)).applyErrorHandling(any(Mono.class));
    }

}
