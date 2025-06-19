package com.bootcamp.project.infraestructure.entrypoints.bootcamp.handler;

import com.bootcamp.project.domain.api.BootcampServicePort;
import com.bootcamp.project.domain.enums.TechnicalMessage;
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
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.capability.response.CapabilityListTechnologyResponse;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
        BootcampDto bootcampDto = new BootcampDto(); // Ajusta si necesitas campos específicos
        Bootcamp bootcamp = Bootcamp.builder()
                .id(1L)
                .name("Java Camp")
                .build();

        // Simulación de entidad de dominio devuelta por el servicio
        BootcampListCapabilityTechnology domainEntity = BootcampListCapabilityTechnology.builder()
                .id(1L)
                .name("Java Camp")
                .capabilities(List.of())
                .build();

        // Simulación de respuesta a enviar al cliente
        BootcampListCapabilityTechnologyResponse capabilityResponse =
                BootcampListCapabilityTechnologyResponse.builder()
                        .id(1L)
                        .name("Java Camp")
                        .capabilities(List.of()) // o agrega mocks si quieres
                        .build();


        // Simulamos que validateRequestSave retorna un BootcampDto.
        when(validateRequestSave.validateAndMapRequest(any(ServerRequest.class)))
                .thenReturn(Flux.just(bootcampDto));

        // Se mapea el DTO a la entidad de dominio.
        when(bootcampMapper.toBootcamp(any(BootcampDto.class))).thenReturn(bootcamp);

        when(bootcampServicePort.saveBootcampCapability(anyList()))
                .thenReturn(Mono.just(List.of(domainEntity)));

        when(bootcampMapperResponse.toBootcampListCapabilityTechnologyResponse(any(BootcampListCapabilityTechnology.class)))
                .thenReturn(capabilityResponse);

        when(applyErrorHandler.applyErrorHandling(any(Mono.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act & Assert
        webTestClient.post()
                .uri(PATH_POST_BOOTCAMP_RELATE_CAPABILITIES)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("[{}]") // JSON de ejemplo, ajusta según tu BootcampDto real
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ApiBootcampCapabilityTechnologyResponseList.class)
                .consumeWith(response -> {
                    ApiBootcampCapabilityTechnologyResponseList body = response.getResponseBody();
                    assertThat(body).isNotNull();
                    assertThat(body.getCode()).isEqualTo(TechnicalMessage.BOOTCAMP_CAPABILITY_RELATION.getCode());
                    assertThat(body.getMessage()).isEqualTo(TechnicalMessage.BOOTCAMP_CAPABILITY_RELATION.getMessage());

                    // 🔧 Aquí la solución explícita con cast:
                    List<BootcampListCapabilityTechnologyResponse> data = (List<BootcampListCapabilityTechnologyResponse>) body.getData();
                    assertThat(data).isNotNull();
                    assertThat(data.size()).isEqualTo(1); // <-- en lugar de .hasSize(1)

                    BootcampListCapabilityTechnologyResponse item = data.get(0);
                    assertThat(item.getId()).isEqualTo(1L);
                    assertThat(item.getName()).isEqualTo("Java Camp");

                    List<CapabilityListTechnologyResponse> capabilities = item.getCapabilities();
                });

        // Verificaciones de invocaciones
        verify(validateRequestSave, times(1)).validateAndMapRequest(any(ServerRequest.class));
        verify(bootcampMapper, times(1)).toBootcamp(any(BootcampDto.class));
        verify(bootcampServicePort, times(1)).saveBootcampCapability(anyList());
        verify(bootcampMapperResponse, times(1)).toBootcampListCapabilityTechnologyResponse(any(BootcampListCapabilityTechnology.class));
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
