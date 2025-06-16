package com.bootcamp.project.infraestructure.persistenceadapter.webclient.capability;

import com.bootcamp.project.domain.model.webclient.capability.api.ApiCapabilityList;
import com.bootcamp.project.domain.model.webclient.capability.api.ApiCapabilityListTechnology;
import com.bootcamp.project.domain.model.webclient.capability.api.ApiCapabilityMessage;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.capability.response.api.ApiCapabilityListResponse;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.capability.response.api.ApiCapabilityListTechnologyResponse;
import com.bootcamp.project.domain.model.webclient.capability.api.ApiMapCapability;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.capability.mapper.CapabilityWebClientMapper;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.capability.response.api.ApiMapCapabilityResponse;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.capability.response.api.ApiCapabilityMessageResponse;
import com.bootcamp.project.infraestructure.persistenceadapter.webclient.util.SendTokenWebClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CapabilityClientTest {
    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    // Mocks para el chain de POST
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    // Mocks para el chain de GET
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    CapabilityWebClientMapper capabilityWebClientMapper;

    private CapabilityClient capabilityClient;

    @BeforeEach
    public void setUp() {
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.filter(any())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        SendTokenWebClient sendTokenWebClient = new SendTokenWebClient();

        capabilityClient = new CapabilityClient(webClientBuilder,
                sendTokenWebClient,
                capabilityWebClientMapper
                );
    }

    @Test
    public void testGetCapabilitiesByBootcampIds() {
        ApiMapCapabilityResponse dummyResponse = ApiMapCapabilityResponse.builder()
                .code("200").message("Success").date("2025-06-03")
                .data(new HashMap<>()).build();

        ApiMapCapability mappedResponse = ApiMapCapability.builder()
                .code("200").message("Success").date("2025-06-03")
                .data(new HashMap<>()).build();

        List<Long> bootcampIds = List.of(1L, 2L, 3L);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ApiMapCapabilityResponse.class)).thenReturn(Mono.just(dummyResponse));

        when(capabilityWebClientMapper.toApiBootcampCapability(dummyResponse)).thenReturn(mappedResponse);

        StepVerifier.create(capabilityClient.getCapabilitiesByBootcampIds(bootcampIds))
                .expectNext(mappedResponse)
                .verifyComplete();
    }

    @Test
    public void testGetCapabilitiesByIds() {
        ApiCapabilityListResponse dummyResponse = ApiCapabilityListResponse.builder()
                .code("200").message("OK").date("2025-06-03").data(List.of()).build();

        ApiCapabilityList mappedList = ApiCapabilityList.builder()
                .code("200").message("OK").date("2025-06-03").data(List.of()).build();

        List<Long> capabilityIds = List.of(100L, 200L);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ApiCapabilityListResponse.class)).thenReturn(Mono.just(dummyResponse));

        when(capabilityWebClientMapper.toApiCapabilityList(dummyResponse)).thenReturn(mappedList);

        StepVerifier.create(capabilityClient.getCapabilitiesByIds(capabilityIds))
                .expectNext(mappedList)
                .verifyComplete();
    }


    @Test
    public void testSaveRelateCapabilitiesBootcamp() {
        ApiMapCapabilityResponse dummyResponse = ApiMapCapabilityResponse.builder()
                .code("201").message("Created").date("2025-06-03")
                .data(new HashMap<>()).build();

        ApiMapCapability mapped = ApiMapCapability.builder()
                .code("201").message("Created").date("2025-06-03")
                .data(new HashMap<>()).build();

        Long bootcampId = 10L;
        List<Long> capabilityIds = List.of(100L, 101L);

        Map<String, Object> expectedRequestBody = new HashMap<>();
        expectedRequestBody.put("bootcampId", bootcampId);
        expectedRequestBody.put("capabilityIds", capabilityIds);

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("api/v1/capability-bootcamp")).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(expectedRequestBody)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ApiMapCapabilityResponse.class)).thenReturn(Mono.just(dummyResponse));

        when(capabilityWebClientMapper.toApiBootcampCapability(dummyResponse)).thenReturn(mapped);

        StepVerifier.create(capabilityClient.saveRelateCapabilitiesBootcamp(bootcampId, capabilityIds))
                .expectNext(mapped)
                .verifyComplete();
    }

    @Test
    public void testGetCapabilitiesTechnologiesByIds() {
        ApiCapabilityListTechnologyResponse dummyResponse = ApiCapabilityListTechnologyResponse.builder().build();

        ApiCapabilityListTechnology mapped = ApiCapabilityListTechnology.builder().build();

        List<Long> capabilityIds = List.of(500L, 600L);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ApiCapabilityListTechnologyResponse.class)).thenReturn(Mono.just(dummyResponse));

        when(capabilityWebClientMapper.toApiCapabilityListTechnology(dummyResponse)).thenReturn(mapped);

        StepVerifier.create(capabilityClient.getCapabilitiesTechnologiesByIds(capabilityIds))
                .expectNext(mapped)
                .verifyComplete();
    }
}
