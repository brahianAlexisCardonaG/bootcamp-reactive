package com.bootcamp.project.domain.usecase.bootcamp;

import com.bootcamp.project.domain.api.BootcampServicePort;
import com.bootcamp.project.domain.enums.TechnicalMessage;
import com.bootcamp.project.domain.exception.BusinessException;
import com.bootcamp.project.domain.model.bootcamp.Bootcamp;
import com.bootcamp.project.domain.model.bootcamp.BootcampListCapabilityTechnology;
import com.bootcamp.project.domain.model.bootcampmongo.BootcampMongo;
import com.bootcamp.project.domain.model.webclient.capability.*;
import com.bootcamp.project.domain.spi.bootcamp.BootcampPersistencePort;
import com.bootcamp.project.domain.spi.bootcampmongo.BootcampMongoPersistencePort;
import com.bootcamp.project.domain.spi.webclient.CapabilityWebClientPort;
import com.bootcamp.project.domain.spi.webclient.TechnologyWebClientPort;
import com.bootcamp.project.domain.usecase.bootcamp.util.CapabilityOrder;
import com.bootcamp.project.domain.usecase.bootcamp.util.CapabilityPaginator;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BootcampUseCase implements BootcampServicePort {

    private final BootcampPersistencePort bootcampPersistencePort;
    private final TransactionalOperator transactionalOperator;
    private final CapabilityWebClientPort capabilityWebClientPort;
    private final TechnologyWebClientPort technologyWebClientPort;
    private final BootcampMongoPersistencePort bootcampMongoPersistencePort;


    @Override
    public Mono<List<BootcampListCapabilityTechnology>> saveBootcampCapability(Flux<Bootcamp> bootcampFlux) {
        return transactionalOperator.transactional(

                // 1) Por cada Bootcamp entrante, validar y persistir
                bootcampFlux
                        // 1.1) Validar que no exista otro bootcamp con el mismo nombre
                        .flatMap(incomingBootcamp ->
                                bootcampPersistencePort.findByName(incomingBootcamp.getName())
                                        .flatMap(exists -> {
                                            if (exists) {
                                                return Mono.error(
                                                        new BusinessException(TechnicalMessage.CAPABILITY_ALREADY_EXISTS)
                                                );
                                            }
                                            return Mono.just(incomingBootcamp);
                                        })
                        )
                        // 1.2) Validar que los capabilityIds realmente existan
                        .flatMap(validBootcamp ->
                                capabilityWebClientPort.getCapabilitiesByIds(validBootcamp.getCapabilityIds())
                                        .flatMap(capResp -> {
                                            if (capResp == null
                                                    || capResp.getData() == null
                                                    || capResp.getData().isEmpty()
                                            ) {
                                                return Mono.error(
                                                        new BusinessException(TechnicalMessage.CAPABILITIES_NOT_EXISTS)
                                                );
                                            }
                                            return Mono.just(validBootcamp);
                                        })
                        )
                        // 1.3) Guardar el bootcamp y relacionar capabilities
                        .flatMap(validBootcamp ->
                                bootcampPersistencePort.save(Flux.just(validBootcamp))
                                        .next()
                                        .flatMap(savedEntity -> {
                                            // savedEntity proviene de la BD (tiene id, name, releaseDate, duration)
                                            // validBootcamp tiene la lista capabilityIds original
                                            Bootcamp merged = new Bootcamp(
                                                    savedEntity.getId(),
                                                    savedEntity.getName(),
                                                    savedEntity.getReleaseDate(),
                                                    savedEntity.getDuration(),
                                                    validBootcamp.getCapabilityIds()
                                            );

                                            // Guardamos las relaciones en el cliente
                                            return capabilityWebClientPort
                                                    .saveRelateCapabilitiesBootcamp(
                                                            merged.getId(),
                                                            merged.getCapabilityIds()
                                                    )
                                                    .thenReturn(merged);
                                        })
                        )
                        .collectList()
                        // 3) A partir de la lista de Bootcamp guardados, construimos la lista de BootcampListCapabilityTechnology
                        .flatMap(savedBootcamps -> {
                            // 3.1) Extraer todos los capabilityIds únicos de los bootcamps guardados
                            List<Long> allSavedCapabilityIds = savedBootcamps.stream()
                                    .flatMap(b -> b.getCapabilityIds().stream())
                                    .distinct()
                                    .toList();

                            // 3.2) Si no hay capabilityIds, lista vacía
                            if (allSavedCapabilityIds.isEmpty()) {
                                return Mono.just(Collections.emptyList());
                            }
                            // 4) Llamar a getCapabilitiesTechnologiesByIds(...)
                            return capabilityWebClientPort
                                    .getCapabilitiesTechnologiesByIds(allSavedCapabilityIds)
                                    .flatMap(ctResponse -> {
                                        if (ctResponse == null
                                                || ctResponse.getData() == null
                                            // getData() es List<CapabilityTechnology> al ajustar el DTO
                                        ) {
                                            return Mono.error(new BusinessException(TechnicalMessage
                                                    .CAPABILITIES_NOT_EXISTS));
                                        }

                                        // 5) Obtengo la lista de CapabilityTechnology y la indexo por ID
                                        List<CapabilityTechnology> techList = ctResponse.getData();

                                        Map<Long, CapabilityTechnology> capabilityTechById = techList.stream()
                                                .filter(ct -> ct.getId() != null)
                                                .collect(Collectors.toMap(
                                                        CapabilityTechnology::getId,
                                                        ct -> ct,
                                                        (existing, replacement) -> existing // si hay duplicados, conservar el primero
                                                ));

                                        // 6) Construir la lista final de BootcampListCapabilityTechnology
                                        List<BootcampListCapabilityTechnology> resultList = savedBootcamps.stream()
                                                .map(savedBootcamp -> {
                                                    Long bcId = savedBootcamp.getId();
                                                    String bcName = savedBootcamp.getName();

                                                    // Obtenemos los CapabilityTechnology que correspondan a este bootcamp
                                                    List<CapabilityTechnology> capsForThisBootcamp = savedBootcamp.getCapabilityIds().stream()
                                                            .map(capabilityTechById::get)
                                                            .filter(Objects::nonNull)
                                                            .toList();

                                                    return new BootcampListCapabilityTechnology(
                                                            bcId,
                                                            bcName,
                                                            capsForThisBootcamp
                                                    );
                                                })
                                                .toList();
                                        return Mono.just(resultList);
                                    })
                                    .flatMap(resultList -> {
                                        List<BootcampMongo> bootcampMongoList = savedBootcamps.stream()
                                                .map(savedBootcamp -> {
                                                    // Obtener las capabilities con tecnologías correspondientes a este bootcamp
                                                    List<CapabilityTechnology> capsForThisBootcamp = resultList.stream()
                                                            .filter(r -> r.getId().equals(savedBootcamp.getId()))
                                                            .findFirst()
                                                            .map(BootcampListCapabilityTechnology::getCapabilities)
                                                            .orElse(List.of());

                                                    int numCapabilities = capsForThisBootcamp.size();
                                                    int numTechnologies = capsForThisBootcamp.stream()
                                                            .mapToInt(cap -> cap.getTechnologies() != null ? cap.getTechnologies().size() : 0)
                                                            .sum();

                                                    return BootcampMongo.builder()
                                                            .id(savedBootcamp.getId())
                                                            .idBootcamp(savedBootcamp.getId())
                                                            .name(savedBootcamp.getName())
                                                            .releaseDate(savedBootcamp.getReleaseDate())
                                                            .duration(savedBootcamp.getDuration())
                                                            .numberCapabilities(numCapabilities)
                                                            .numberTechnologies(numTechnologies)
                                                            .numberPersons(0)
                                                            .build();
                                                })
                                                .toList();

                                        return bootcampMongoPersistencePort.saveAll(Flux.fromIterable(bootcampMongoList))
                                                .thenReturn(resultList);
                                    });
                        })
        );
    }

    @Override
    public Mono<List<BootcampListCapabilityTechnology>> findCapabilitiesByIdBootcamps(List<Long> bootcampIds, String order, int skip, int rows) {

        return bootcampPersistencePort.findByAllIds(bootcampIds)
                .flatMap(bootcamps -> {
                    // 1) Validar existencia de todos los bootcamps
                    if (bootcamps.size() != bootcampIds.size()) {
                        return Mono.error(new BusinessException(TechnicalMessage.BOOTCAMP_NOT_EXISTS));
                    }

                    // 2) Obtener CapabilityResponse por cada bootcamp
                    return capabilityWebClientPort.getCapabilitiesByBootcampIds(bootcampIds)
                            .flatMap(response -> {
                                if (response == null || response.getData() == null) {
                                    return Mono.error(new RuntimeException("Technology o data es null"));
                                }

                                // 2.1) Convertir Map<String, List<Capability>> → Map<Long, List<Capability>>
                                Map<Long, List<Capability>> capabilitiesByBootcamp = response.getData()
                                        .entrySet().stream()
                                        .collect(Collectors.toMap(
                                                entry -> Long.valueOf(entry.getKey()),  // clave String→Long
                                                Map.Entry::getValue                     // valor = List<CapabilityResponse>
                                        ));

                                // 3) Extraer lista de todos los capabilityIds únicos
                                List<Long> allCapabilityIds = capabilitiesByBootcamp.values().stream()
                                        .flatMap(List::stream)               // aplanar listas de CapabilityResponse
                                        .map(Capability::getId)      // tomar cada capabilityId
                                        .distinct()                          // eliminar duplicados
                                        .toList();

                                // 4) Hacer UNA SOLA llamada: obtener CapabilityTechnology (lista) de esos IDs
                                return capabilityWebClientPort
                                        .getCapabilitiesTechnologiesByIds(allCapabilityIds)
                                        .map(ctResponse -> {
                                            // 4.1) Ahora ctResponse.getData() es List<CapabilityTechnology>
                                            List<CapabilityTechnology> techList = ctResponse.getData();

                                            // 4.2) Construir mapa para indexar por capabilityId → CapabilityTechnology
                                            Map<Long, CapabilityTechnology> capabilityTechById = techList.stream()
                                                    .filter(ct -> ct.getId() != null) // descartamos posibles null
                                                    .collect(Collectors.toMap(
                                                            CapabilityTechnology::getId,
                                                            ct -> ct,
                                                            (existing, replacement) -> existing // si hay duplicados, quedarnos con el primero
                                                    ));

                                            // 5) Armar lista de BootcampListCapabilityTechnology
                                            List<BootcampListCapabilityTechnology> bootcampList = capabilitiesByBootcamp.entrySet().stream()
                                                    .map(entry -> {
                                                        Long bootcampId = entry.getKey();

                                                        // 5.1) Buscar el nombre del bootcamp en la lista “bootcamps”
                                                        String bootcampName = bootcamps.stream()
                                                                .filter(b -> b.getId().equals(bootcampId))
                                                                .findFirst()
                                                                .map(Bootcamp::getName)
                                                                .orElse("null");

                                                        // 5.2) Para cada Capability de este bootcamp, obtener el CapabilityTechnology
                                                        List<CapabilityTechnology> capabilities = entry.getValue().stream()
                                                                .map(capResp -> capabilityTechById.get(capResp.getId()))
                                                                .filter(Objects::nonNull)
                                                                .toList();

                                                        return new BootcampListCapabilityTechnology(
                                                                bootcampId,
                                                                bootcampName,
                                                                capabilities
                                                        );
                                                    })
                                                    .toList();

                                            // 6) Ordenar según 'order'
                                            List<BootcampListCapabilityTechnology> ordered =
                                                    CapabilityOrder.sortList(bootcampList, order);

                                            // 7) Paginar según 'skip' y 'rows'
                                            return CapabilityPaginator.paginateList(ordered, skip, rows);
                                        });
                            });
                });
    }

    @Override
    public Mono<Void> deleteBootcamps(List<Long> bootcampIds) {
        return transactionalOperator.transactional(bootcampPersistencePort.findByAllIds(bootcampIds)
                .flatMap(bootcamps -> {
                    if (bootcamps.size() != bootcampIds.size()) {
                        return Mono.error(
                                new BusinessException(TechnicalMessage.BOOTCAMPS_NOT_EXISTS)
                        );
                    }
                    return capabilityWebClientPort.getCapabilitiesByBootcampIds(bootcampIds);
                }).flatMap(apiResponse ->
                        // Convertimos el Map en un Flux para iterar sobre cada entrada (cada bootcamp y su lista de capacidades)
                        Flux.fromIterable(apiResponse.getData().entrySet())
                                .concatMap(entry -> {
                                    // Para cada bootcamp, obtenemos su lista de capacidades
                                    List<Capability> capabilities = entry.getValue();
                                    // Extraemos los IDs de las capacidades de esa lista
                                    List<Long> capabilityIds = capabilities.stream()
                                            .map(Capability::getId)
                                            .collect(Collectors.toList());
                                    // Se invoca el metodo que elimina las capacidades de este bootcamp
                                    return capabilityWebClientPort.deleteBootcampCapabilities(capabilityIds)
                                            .thenReturn(capabilityIds);
                                })
                                // Una vez procesados todos los elementos, finalizamos el flujo
                                .collectList()  // Consolidamos los capabilityIds (podrían repetirse, por lo que se filtran luego)
                                .flatMapMany(capabilityIdsList -> {
                                    // Aplanamos la lista resultante y eliminamos duplicados
                                    List<Long> allCapabilityIds = capabilityIdsList.stream()
                                            .flatMap(List::stream)
                                            .distinct()
                                            .collect(Collectors.toList());
                                    // Con los capabilityIds consolidados, consultamos las technologies asociadas
                                    return technologyWebClientPort.getTechnologiesByCapabilityIds(allCapabilityIds);
                                })
                                .flatMap(techApiResponse ->
                                        Flux.fromIterable(techApiResponse.getData().entrySet())
                                                .concatMap(entry -> {
                                                    // Para cada capability, obtenemos su lista de TechnologyResponse
                                                    return Flux.fromIterable(entry.getValue())
                                                            // Si deseas procesar cada technology individualmente (por ejemplo, primero el id 5, luego el 4 y luego el 3)
                                                            .concatMap(technology -> {
                                                                // Llamamos a deleteCapabilityTechnologies pasando una lista con el id de la technology
                                                                return technologyWebClientPort.deleteCapabilityTechnologies(
                                                                        Collections.singletonList(technology.getId())
                                                                );
                                                            });
                                                })
                                )
                                .then()
                )
        );
    }

    @Override
    public Flux<Bootcamp> getBootcampsByIds(List<Long> bootcampIds) {
        return bootcampPersistencePort.findByAllIds(bootcampIds)
                .flatMapMany(list -> {
                    if (list.isEmpty()) {
                        return Flux.error(
                                new BusinessException(TechnicalMessage.BOOTCAMPS_NOT_EXISTS)
                        );
                    }
                    return Flux.fromIterable(list);
                });
    }
}