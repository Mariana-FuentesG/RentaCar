package com.prueba.ms_clientes.controller;

import com.prueba.ms_clientes.assembler.DireccionModelAssembler;
import com.prueba.ms_clientes.dto.DireccionDTO;
import com.prueba.ms_clientes.service.DireccionService;
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
@RequestMapping("/api/v1/direcciones")
@Tag(name = "Direcciones", description = "Gestión de direcciones asociadas a clientes de RentaCar")
public class DireccionController {

    @Autowired
    DireccionService direccionService;

    @Autowired
    DireccionModelAssembler direccionModelAssembler;

    // GET → LISTAR TODAS LAS DIRECCIONES
    @GetMapping
    @Operation(summary = "Obtener todas las direcciones",
            description = "Retorna el listado completo de direcciones con enlaces HATEOAS")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de direcciones obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DireccionDTO.class)))
    })
    public ResponseEntity<CollectionModel<EntityModel<DireccionDTO>>> obtenerDirecciones() {
        List<EntityModel<DireccionDTO>> direcciones = direccionService.obtenerDirecciones()
                .stream()
                .map(direccionModelAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<DireccionDTO>> coleccion = CollectionModel.of(
                direcciones,
                linkTo(methodOn(DireccionController.class).obtenerDirecciones()).withSelfRel()
        );

        return ResponseEntity.ok(coleccion);
    }

    // GET → OBTENER DIRECCION POR ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener dirección por ID",
            description = "Retorna una dirección específica con enlaces HATEOAS")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dirección encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DireccionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Dirección no encontrada")
    })
    public ResponseEntity<EntityModel<DireccionDTO>> obtenerDireccionPorId(
            @Parameter(description = "ID de la dirección", required = true, example = "1")
            @PathVariable Integer id) {
        DireccionDTO direccion = direccionService.obtenerDireccionPorId(id);
        if (direccion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(direccionModelAssembler.toModel(direccion));
    }

    // GET → BUSCAR DIRECCIONES POR COMUNA
    @GetMapping("/comuna/{comuna}")
    @Operation(summary = "Buscar direcciones por comuna",
            description = "Retorna direcciones que coincidan con la comuna indicada")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Direcciones encontradas",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DireccionDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron direcciones")
    })
    public ResponseEntity<CollectionModel<EntityModel<DireccionDTO>>> buscarPorComuna(
            @Parameter(description = "Nombre de la comuna", example = "Santiago")
            @PathVariable String comuna) {
        List<DireccionDTO> direcciones = direccionService.buscarPorComuna(comuna);
        if (direcciones.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<EntityModel<DireccionDTO>> direccionesConLinks = direcciones.stream()
                .map(direccionModelAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(direccionesConLinks,
                linkTo(methodOn(DireccionController.class)
                        .buscarPorComuna(comuna)).withSelfRel()));
    }

    // POST → CREAR DIRECCION
    @PostMapping
    @Operation(summary = "Crear nueva dirección",
            description = "Registra una nueva dirección asociada a un cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Dirección creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DireccionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<EntityModel<DireccionDTO>> guardar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la dirección a crear",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DireccionDTO.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de Dirección",
                                    summary = "Nueva dirección",
                                    value = """
                                            {
                                              "calle": "Av. Providencia",
                                              "numeroCasa": 1234,
                                              "comuna": "Providencia",
                                              "ciudad": "Santiago",
                                              "codigoPostal": 7500000,
                                              "estado": true,
                                              "fechaRegistro": "2026-06-20",
                                              "clienteId": 1
                                            }
                                            """
                            )
                    ))
            @Valid @RequestBody DireccionDTO dto) {
        DireccionDTO creada = direccionService.guardarDireccion(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(direccionModelAssembler.toModel(creada));
    }

    // PUT → ACTUALIZAR DIRECCION
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar dirección existente",
            description = "Actualiza los datos de una dirección por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dirección actualizada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DireccionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Dirección no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<EntityModel<DireccionDTO>> actualizar(
            @Parameter(description = "ID de la dirección", required = true, example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados de la dirección",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DireccionDTO.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de Actualización",
                                    summary = "Actualiza datos de la dirección",
                                    value = """
                                            {
                                              "calle": "Av. Las Condes",
                                              "numeroCasa": 5678,
                                              "comuna": "Las Condes",
                                              "ciudad": "Santiago",
                                              "codigoPostal": 7550000,
                                              "estado": true,
                                              "fechaRegistro": "2026-06-20",
                                              "clienteId": 1
                                            }
                                            """
                            )
                    ))
            @Valid @RequestBody DireccionDTO dto) {
        DireccionDTO actualizada = direccionService.actualizarDireccion(id, dto);
        if (actualizada == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(direccionModelAssembler.toModel(actualizada));
    }

    // DELETE → ELIMINAR DIRECCION
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar dirección",
            description = "Elimina una dirección por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Dirección eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Dirección no encontrada"),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar, tiene registros asociados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la dirección", required = true, example = "1")
            @PathVariable Integer id) {
        boolean eliminado = direccionService.eliminarDireccion(id);
        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}