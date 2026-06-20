package com.prueba.ms_reservas.controller;

import com.prueba.ms_reservas.assembler.EstadoReservaModelAssembler;
import com.prueba.ms_reservas.dto.EstadoReservaDTO;
import com.prueba.ms_reservas.service.EstadoReservaService;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/estados-reserva")
@Tag(name = "Estados de Reserva",description = "Gestión de los estados posibles de una reserva (Pendiente, Confirmada, Pagada, etc.)")
public class EstadoReservaController {

    @Autowired
    EstadoReservaService estadoReservaService;

    @Autowired
    EstadoReservaModelAssembler estadoReservaModelAssembler;

    // GET → LISTAR TODOS LOS ESTADOS
    @GetMapping
    @Operation(summary = "Obtener todos los Estados de Reserva", description = "Obtiene una lista de todos los estados de reserva existentes, cada uno con sus enlaces HATEOAS")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación Exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EstadoReservaDTO.class)))
    })
    public ResponseEntity<CollectionModel<EntityModel<EstadoReservaDTO>>> obtenerEstadosReserva(){
        List<EntityModel<EstadoReservaDTO>> estados = estadoReservaService.obtenerEstadosReserva()
                .stream()
                .map(estadoReservaModelAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<EstadoReservaDTO>> coleccion = CollectionModel.of(
                estados,
                linkTo(methodOn(EstadoReservaController.class).obtenerEstadosReserva()).withSelfRel()
        );

        return ResponseEntity.ok(coleccion);
    }

    // GET → BUSCAR POR ID
    @GetMapping("/{id}")
    @Operation(summary = "Buscar Estado de Reserva por ID", description = "Obtiene un estado de reserva mediante su identificación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado Encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EstadoReservaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Estado no Encontrado")
    })
    public ResponseEntity<EntityModel<EstadoReservaDTO>> obtenerEstadoReservaPorId(
            @Parameter(description = "ID del estado de reserva",required = true)
            @PathVariable Integer id){
        EstadoReservaDTO estado = estadoReservaService
                        .obtenerEstadoReservaPorId(id);
        if(estado == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(estadoReservaModelAssembler.toModel(estado));
    }

    // POST → GUARDAR
    @PostMapping
    @Operation(summary = "Crear Estado de Reserva", description = "Permite registrar un nuevo estado de reserva")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Estado Creado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EstadoReservaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos Inválidos")
    })
    public ResponseEntity<EntityModel<EstadoReservaDTO>> guardarEstadoReserva(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para crear un estado de reserva",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EstadoReservaDTO.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de Estado",
                                    summary = "Nuevo estado de reserva",
                                    value = """
                                            {
                                              "nombreEstado": "Pendiente",
                                              "prioridad": 1,
                                              "activo": true,
                                              "fechaCreacion": "2026-06-16",
                                              "diasLimitePago": 3
                                            }
                                            """
                            )
                    ))
            @Valid @RequestBody EstadoReservaDTO dto){
        EstadoReservaDTO guardado = estadoReservaService.guardarEstadoReserva(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(estadoReservaModelAssembler.toModel(guardado));
    }

    // PUT → ACTUALIZAR
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar Estado de Reserva", description = "Actualiza un estado de reserva existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado Actualizado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EstadoReservaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Estado no Encontrado")
    })
    public ResponseEntity<EntityModel<EstadoReservaDTO>> actualizarEstadoReserva(
            @Parameter(description = "ID del estado de reserva", example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos para actualizar el estado de reserva",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EstadoReservaDTO.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de Actualización",
                                    summary = "Desactiva un estado de reserva",
                                    value = """
                                            {
                                              "nombreEstado": "Cancelada",
                                              "prioridad": 4,
                                              "activo": false,
                                              "fechaCreacion": "2026-06-16",
                                              "diasLimitePago": 1
                                            }
                                            """
                            )
                    ))
            @Valid @RequestBody EstadoReservaDTO dto) {
        EstadoReservaDTO actualizado = estadoReservaService
                .actualizarEstadoReserva(id, dto);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(estadoReservaModelAssembler.toModel(actualizado));
    }

    // DELETE → ELIMINAR
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar Estado de Reserva", description = "Elimina un estado de reserva mediante su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Estado eliminado Correctamente"),
            @ApiResponse(responseCode = "404", description = "Estado no Encontrado"),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar, tiene reservas asociadas"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")

    })
    public ResponseEntity<Void> eliminarEstadoReserva(
            @Parameter(description = "ID del estado de reserva", example = "1")
            @PathVariable Integer id) {
        boolean eliminado = estadoReservaService
                .eliminarEstadoReserva(id);
        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}