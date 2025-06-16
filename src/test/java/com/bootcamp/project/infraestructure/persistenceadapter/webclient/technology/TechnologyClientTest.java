package com.bootcamp.project.infraestructure.persistenceadapter.webclient.technology;

import com.bootcamp.project.domain.model.webclient.technology.api.ApiTechnologyMap;
import com.bootcamp.project.domain.model.webclient.technology.api.ApiTechnologyMessage;
import com.bootcamp.project.infraestructure.entrypoints.bootcamp.response.api.ApiResponseBase;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.technology.mapper.TechnologyWebClientMapper;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.technology.response.api.ApiTechnologyMapResponse;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.technology.response.api.ApiTechnologyMessageResponse;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.util.SendTokenWebClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TechnologyClientTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    // Mocks para el chain de GET
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    TechnologyWebClientMapper technologyWebClientMapper;

    private TechnologyClient technologyClient;


    @BeforeEach
    public void setUp() {
        // Configuramos el builder para que al invocar baseUrl(...) y build() retorne el webClient mockeado.
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.filter(any())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        SendTokenWebClient sendTokenWebClient = new SendTokenWebClient();
        technologyClient = new TechnologyClient(webClientBuilder, sendTokenWebClient,technologyWebClientMapper);
    }

    @Test
    public void testGetTechnologiesByCapabilityIds() {
        List<Long> capabilityIds = List.of(1L, 2L);

        ApiTechnologyMapResponse dummyResponse = ApiTechnologyMapResponse.builder()
                .code("200")
                .message("OK")
                .date("2025-06-13")
                .data(Map.of())
                .build();

        ApiTechnologyMap mapped = ApiTechnologyMap.builder()
                .code("200")
                .message("OK")
                .date("2025-06-13")
                .data(Map.of())
                .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ApiTechnologyMapResponse.class)).thenReturn(Mono.just(dummyResponse));
        when(technologyWebClientMapper.toApiTechnologyMap(dummyResponse)).thenReturn(mapped);

        StepVerifier.create(technologyClient.getTechnologiesByCapabilityIds(capabilityIds))
                .expectNext(mapped)
                .verifyComplete();

        verify(webClient).get();
        verify(requestHeadersUriSpec).uri(any(Function.class));
        verify(requestHeadersSpec).retrieve();
        verify(responseSpec).bodyToMono(ApiTechnologyMapResponse.class);
    }

    @Test
    public void testDeleteCapabilityTechnologies() {
        List<Long> technologyIds = List.of(1L, 2L, 3L);

        ApiTechnologyMessageResponse dummyResponse = ApiTechnologyMessageResponse.builder()
                .code("200")
                .message("Deleted")
                .date("2025-06-13")
                .build();

        ApiTechnologyMessage mapped = ApiTechnologyMessage.builder()
                .code("200")
                .message("Deleted")
                .date("2025-06-13")
                .build();

        when(webClient.delete()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ApiTechnologyMessageResponse.class)).thenReturn(Mono.just(dummyResponse));
        when(technologyWebClientMapper.toApiTechnologyMessage(dummyResponse)).thenReturn(mapped);

        StepVerifier.create(technologyClient.deleteCapabilityTechnologies(technologyIds))
                .expectNext(mapped)
                .verifyComplete();

        verify(webClient).delete();
        verify(requestHeadersUriSpec).uri(any(Function.class));
        verify(requestHeadersSpec).retrieve();
        verify(responseSpec).bodyToMono(ApiTechnologyMessageResponse.class);
    }

}
