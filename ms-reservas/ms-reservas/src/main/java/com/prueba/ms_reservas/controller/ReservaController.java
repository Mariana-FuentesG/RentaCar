package com.prueba.ms_reservas.controller;

import com.prueba.ms_reservas.dto.ReservaDTO;
import com.prueba.ms_reservas.service.ReservaService;
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
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v1/reservas")
@Tag(name = "Reservas", description = "Gestión de reservas de vehículos")
public class ReservaController {

    @Autowired
    ReservaService reservaService;


    // GET → LISTAR TODAS LAS RESERVAS
    @GetMapping
    @Operation(summary = "Obtener todas las reservas",
            description = "Retorna el listado completo de reservas con enlaces HATEOAS")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de reservas obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReservaDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<CollectionModel<EntityModel<ReservaDTO>>> obtenerReservas() {
        List<ReservaDTO> reservas = reservaService.obtenerReservas();

        List<EntityModel<ReservaDTO>> reservasConLinks = reservas.stream()
                .map(reserva -> EntityModel.of(reserva,
                        linkTo(methodOn(ReservaController.class)
                                .obtenerReservaPorId(reserva.getId())).withSelfRel(),
                        linkTo(methodOn(ReservaController.class)
                                .obtenerReservas()).withRel("todas-las-reservas"),
                        linkTo(methodOn(ReservaController.class)
                                .eliminarReserva(reserva.getId())).withRel("eliminar")))
                .collect(Collectors.toList());

        Link selfLink = linkTo(methodOn(ReservaController.class).obtenerReservas()).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(reservasConLinks, selfLink));
    }

    // GET → BUSCAR RESERVA POR ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener reserva por ID",
            description = "Retorna una reserva específica con enlaces HATEOAS")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReservaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<EntityModel<ReservaDTO>> obtenerReservaPorId(
            @Parameter(description = "ID de la reserva", required = true, example = "1")
            @PathVariable Integer id) {

        ReservaDTO reserva = reservaService.obtenerReservaPorId(id);
        if (reserva == null) {
            return ResponseEntity.notFound().build();
        }

        EntityModel<ReservaDTO> entityModel = EntityModel.of(reserva,
                linkTo(methodOn(ReservaController.class)
                        .obtenerReservaPorId(id)).withSelfRel(),
                linkTo(methodOn(ReservaController.class)
                        .obtenerReservas()).withRel("todas-las-reservas"),
                linkTo(methodOn(ReservaController.class)
                        .actualizarReserva(id, null)).withRel("actualizar"),
                linkTo(methodOn(ReservaController.class)
                        .eliminarReserva(id)).withRel("eliminar"),
                linkTo(methodOn(EstadoReservaController.class)
                        .obtenerEstadoReservaPorId(reserva.getEstadoReservaId())).withRel("estado-reserva"));

        return ResponseEntity.ok(entityModel);
    }

    // POST → GUARDAR RESERVA
    @PostMapping
    @Operation(summary = "Crear nueva reserva",
            description = "Crea una reserva validando cliente y vehículo mediante Feign Client")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reserva creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReservaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o cliente/vehículo no encontrado"),
            @ApiResponse(responseCode = "404", description = "Estado de reserva no encontrado")
    })
    public ResponseEntity<EntityModel<ReservaDTO>> guardarReserva(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la reserva a crear",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReservaDTO.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de Reserva",
                                    summary = "Nueva reserva de vehículo",
                                    value = """
                                            {
                                              "clienteId": 1,
                                              "vehiculoId": 1,
                                              "montoReserva": 150000,
                                              "cantidadDias": 3,
                                              "pagada": false,
                                              "fechaInicio": "2026-06-20",
                                              "fechaTermino": "2026-06-23",
                                              "fechaReserva": "2026-06-19",
                                              "observacion": "Cliente solicita entrega en sucursal central",
                                              "estadoReservaId": 1
                                            }
                                            """
                            )
                    ))
            @Valid @RequestBody ReservaDTO dto) {

        ReservaDTO reservaGuardada = reservaService.guardarReserva(dto);
        if (reservaGuardada == null) {
            return ResponseEntity.badRequest().build();
        }

        EntityModel<ReservaDTO> entityModel = EntityModel.of(reservaGuardada,
                linkTo(methodOn(ReservaController.class)
                        .obtenerReservaPorId(reservaGuardada.getId())).withSelfRel(),
                linkTo(methodOn(ReservaController.class)
                        .obtenerReservas()).withRel("todas-las-reservas"));

        return ResponseEntity.status(HttpStatus.CREATED).body(entityModel);
    }

    // PUT → ACTUALIZAR RESERVA
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar reserva existente",
            description = "Actualiza los datos de una reserva por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva actualizada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReservaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<EntityModel<ReservaDTO>> actualizarReserva(
            @Parameter(description = "ID de la reserva a actualizar", required = true, example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados de la reserva",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReservaDTO.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de Actualización",
                                    summary = "Actualiza el estado y observación de la reserva",
                                    value = """
                                            {
                                              "clienteId": 1,
                                              "vehiculoId": 1,
                                              "montoReserva": 150000,
                                              "cantidadDias": 3,
                                              "pagada": true,
                                              "fechaInicio": "2026-06-20",
                                              "fechaTermino": "2026-06-23",
                                              "fechaReserva": "2026-06-19",
                                              "observacion": "Reserva confirmada y pagada",
                                              "estadoReservaId": 2
                                            }
                                            """
                            )
                    ))
            @Valid @RequestBody ReservaDTO dto) {

        ReservaDTO reservaActualizada = reservaService.actualizarReserva(id, dto);
        if (reservaActualizada == null) {
            return ResponseEntity.notFound().build();
        }

        EntityModel<ReservaDTO> entityModel = EntityModel.of(reservaActualizada,
                linkTo(methodOn(ReservaController.class)
                        .obtenerReservaPorId(id)).withSelfRel(),
                linkTo(methodOn(ReservaController.class)
                        .obtenerReservas()).withRel("todas-las-reservas"),
                linkTo(methodOn(ReservaController.class)
                        .eliminarReserva(id)).withRel("eliminar"));

        return ResponseEntity.ok(entityModel);
    }

    // GET → BUSCAR RESERVAS DESDE FECHA
    @GetMapping("/fecha/{fecha}")
    @Operation(summary = "Buscar reservas desde una fecha",
            description = "Retorna reservas con fecha de inicio mayor o igual a la indicada")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reservas encontradas",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReservaDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron reservas para la fecha indicada")
    })
    public ResponseEntity<CollectionModel<EntityModel<ReservaDTO>>> buscarReservasDesdeFecha(
            @Parameter(description = "Fecha desde (formato: YYYY-MM-DD)", example = "2026-06-01")
            @PathVariable LocalDate fecha) {

        List<ReservaDTO> reservas = reservaService.buscarReservasDesdeFecha(fecha);
        if (reservas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<EntityModel<ReservaDTO>> reservasConLinks = reservas.stream()
                .map(reserva -> EntityModel.of(reserva,
                        linkTo(methodOn(ReservaController.class)
                                .obtenerReservaPorId(reserva.getId())).withSelfRel(),
                        linkTo(methodOn(ReservaController.class)
                                .obtenerReservas()).withRel("todas-las-reservas")))
                .collect(Collectors.toList());

        Link selfLink = linkTo(methodOn(ReservaController.class)
                .buscarReservasDesdeFecha(fecha)).withSelfRel();
        Link todasLink = linkTo(methodOn(ReservaController.class)
                .obtenerReservas()).withRel("todas-las-reservas");

        return ResponseEntity.ok(CollectionModel.of(reservasConLinks, selfLink, todasLink));
    }

    // DELETE → ELIMINAR RESERVA
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar reserva",
            description = "Elimina una reserva por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<Void> eliminarReserva(
            @Parameter(description = "ID de la reserva a eliminar", required = true, example = "1")
            @PathVariable Integer id) {

        boolean eliminado = reservaService.eliminarReserva(id);
        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}