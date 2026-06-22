package com.prueba.ms_vehiculos.controller;

import com.prueba.ms_vehiculos.assembler.VehiculoModelAssembler;
import com.prueba.ms_vehiculos.dto.VehiculoDTO;
import com.prueba.ms_vehiculos.service.VehiculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v1/vehiculos")
@Tag(name = "Vehículos", description = "Gestión de vehículos de RentaCar")
public class VehiculoController {

    @Autowired
    VehiculoService vehiculoService;

    @Autowired
    VehiculoModelAssembler vehiculoModelAssembler;

    // GET → LISTAR TODOS LOS VEHICULOS
    @GetMapping
    @Operation(summary = "Obtener todos los vehículos",
            description = "Retorna el listado completo de vehículos con enlaces HATEOAS")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de vehículos obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VehiculoDTO.class)))
    })
    public ResponseEntity<CollectionModel<EntityModel<VehiculoDTO>>> obtenerVehiculos() {
        List<EntityModel<VehiculoDTO>> vehiculos = vehiculoService.obtenerVehiculos()
                .stream()
                .map(vehiculoModelAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<VehiculoDTO>> coleccion = CollectionModel.of(
                vehiculos,
                linkTo(methodOn(VehiculoController.class).obtenerVehiculos()).withSelfRel()
        );

        return ResponseEntity.ok(coleccion);
    }

    // GET → BUSCAR VEHICULO POR ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener vehículo por ID",
            description = "Retorna un vehículo específico con enlaces HATEOAS")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehículo encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VehiculoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    public ResponseEntity<EntityModel<VehiculoDTO>> obtenerVehiculoPorId(
            @Parameter(description = "ID del vehículo", required = true, example = "1")
            @PathVariable Integer id) {
        VehiculoDTO vehiculo = vehiculoService.obtenerVehiculoPorId(id);
        if (vehiculo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vehiculoModelAssembler.toModel(vehiculo));
    }

    // POST → GUARDAR VEHICULO
    @PostMapping
    @Operation(summary = "Crear nuevo vehículo",
            description = "Registra un nuevo vehículo en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Vehículo creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VehiculoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<EntityModel<VehiculoDTO>> guardarVehiculo(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del vehículo a crear",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = VehiculoDTO.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de Vehículo",
                                    summary = "Nuevo vehículo",
                                    value = """
                                            {
                                              "patente": "ABCD12",
                                              "marca": "Toyota",
                                              "modelo": "Corolla",
                                              "precioDiario": 50000.0,
                                              "anio": 2023,
                                              "disponible": true,
                                              "fechaIngreso": "2026-06-20",
                                              "categoriaId": 1
                                            }
                                            """
                            )
                    ))
            @Valid @RequestBody VehiculoDTO dto) {
        VehiculoDTO guardado = vehiculoService.guardarVehiculo(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vehiculoModelAssembler.toModel(guardado));
    }

    // PUT → ACTUALIZAR VEHICULO
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar vehículo existente",
            description = "Actualiza los datos de un vehículo por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehículo actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VehiculoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<EntityModel<VehiculoDTO>> actualizarVehiculo(
            @Parameter(description = "ID del vehículo", required = true, example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados del vehículo",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = VehiculoDTO.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de Actualización",
                                    summary = "Actualiza datos del vehículo",
                                    value = """
                                            {
                                              "patente": "ABCD12",
                                              "marca": "Toyota",
                                              "modelo": "Corolla",
                                              "precioDiario": 55000.0,
                                              "anio": 2023,
                                              "disponible": false,
                                              "fechaIngreso": "2026-06-20",
                                              "categoriaId": 1
                                            }
                                            """
                            )
                    ))
            @Valid @RequestBody VehiculoDTO dto) {
        VehiculoDTO actualizado = vehiculoService.actualizarVehiculo(id, dto);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vehiculoModelAssembler.toModel(actualizado));
    }

    // DELETE → ELIMINAR VEHICULO
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar vehículo",
            description = "Elimina un vehículo por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Vehículo eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado"),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar, tiene registros asociados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> eliminarVehiculo(
            @Parameter(description = "ID del vehículo", required = true, example = "1")
            @PathVariable Integer id) {
        boolean eliminado = vehiculoService.eliminarVehiculo(id);
        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    // GET → BUSCAR VEHICULOS DISPONIBLES POR PRECIO
    @GetMapping("/buscar")
    @Operation(summary = "Buscar vehículos disponibles por precio",
            description = "Retorna vehículos disponibles con precio diario menor al indicado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehículos encontrados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VehiculoDTO.class)))
    })
    public ResponseEntity<CollectionModel<EntityModel<VehiculoDTO>>> obtenerVehiculosDisponiblesPorPrecio(
            @Parameter(description = "Precio máximo diario", example = "100000.0")
            @RequestParam Double precio) {
        List<EntityModel<VehiculoDTO>> vehiculos = vehiculoService
                .obtenerVehiculosDisponiblesPorPrecio(precio)
                .stream()
                .map(vehiculoModelAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(vehiculos,
                linkTo(methodOn(VehiculoController.class)
                        .obtenerVehiculosDisponiblesPorPrecio(precio)).withSelfRel()));
    }
}