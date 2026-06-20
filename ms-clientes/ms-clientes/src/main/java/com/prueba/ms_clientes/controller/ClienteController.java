package com.prueba.ms_clientes.controller;

import com.prueba.ms_clientes.assembler.ClienteModelAssembler;
import com.prueba.ms_clientes.dto.ClienteDTO;
import com.prueba.ms_clientes.service.ClienteService;
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

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v1/clientes")
@Tag(name = "Clientes", description = "Gestión de clientes de RentaCar")
public class ClienteController {

    @Autowired
    ClienteService clienteService;

    @Autowired
    ClienteModelAssembler clienteModelAssembler;

    // GET → LISTAR TODOS LOS CLIENTES
    @GetMapping
    @Operation(summary = "Obtener todos los clientes",
            description = "Retorna el listado completo de clientes con enlaces HATEOAS")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteDTO.class)))
    })
    public ResponseEntity<CollectionModel<EntityModel<ClienteDTO>>> listarClientes() {
        List<EntityModel<ClienteDTO>> clientes = clienteService.obtenerClientes()
                .stream()
                .map(clienteModelAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<ClienteDTO>> coleccion = CollectionModel.of(
                clientes,
                linkTo(methodOn(ClienteController.class).listarClientes()).withSelfRel()
        );

        return ResponseEntity.ok(coleccion);
    }

    // GET → OBTENER CLIENTE POR ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener cliente por ID",
            description = "Retorna un cliente específico con enlaces HATEOAS")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<EntityModel<ClienteDTO>> obtenerClientePorId(
            @Parameter(description = "ID del cliente", required = true, example = "1")
            @PathVariable Integer id) {
        ClienteDTO cliente = clienteService.obtenerClientePorId(id);
        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(clienteModelAssembler.toModel(cliente));
    }

    // GET → BUSCAR CLIENTE POR EMAIL
    @GetMapping("/email/{email}")
    @Operation(summary = "Buscar clientes por email",
            description = "Retorna clientes que coincidan con el email indicado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Clientes encontrados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron clientes")
    })
    public ResponseEntity<CollectionModel<EntityModel<ClienteDTO>>> obtenerClientePorEmail(
            @Parameter(description = "Email del cliente", example = "juan@example.com")
            @PathVariable String email) {
        List<ClienteDTO> clientes = clienteService.obtenerClientesPorEmail(email);
        if (clientes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<EntityModel<ClienteDTO>> clientesConLinks = clientes.stream()
                .map(clienteModelAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(clientesConLinks,
                linkTo(methodOn(ClienteController.class)
                        .obtenerClientePorEmail(email)).withSelfRel()));
    }

    // GET → LISTAR CLIENTES ACTIVOS
    @GetMapping("/activos")
    @Operation(summary = "Obtener clientes activos",
            description = "Retorna todos los clientes con estado activo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Clientes activos encontrados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron clientes activos")
    })
    public ResponseEntity<CollectionModel<EntityModel<ClienteDTO>>> obtenerClientesActivos() {
        List<ClienteDTO> clientes = clienteService.obtenerClientesActivos();
        if (clientes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<EntityModel<ClienteDTO>> clientesConLinks = clientes.stream()
                .map(clienteModelAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(clientesConLinks,
                linkTo(methodOn(ClienteController.class)
                        .obtenerClientesActivos()).withSelfRel()));
    }

    // POST → GUARDAR CLIENTE
    @PostMapping
    @Operation(summary = "Crear nuevo cliente",
            description = "Registra un nuevo cliente en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<EntityModel<ClienteDTO>> guardar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del cliente a crear",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ClienteDTO.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de Cliente",
                                    summary = "Nuevo cliente",
                                    value = """
                                            {
                                              "rut": 12345678,
                                              "nombreCompleto": "Juan Pérez",
                                              "email": "juan@example.com",
                                              "telefono": "912345678",
                                              "activo": true,
                                              "fechaRegistro": "2026-06-20"
                                            }
                                            """
                            )
                    ))
            @Valid @RequestBody ClienteDTO dto) {
        ClienteDTO guardado = clienteService.guardarCliente(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clienteModelAssembler.toModel(guardado));
    }

    // PUT → ACTUALIZAR CLIENTE
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cliente existente",
            description = "Actualiza los datos de un cliente por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<EntityModel<ClienteDTO>> actualizar(
            @Parameter(description = "ID del cliente", required = true, example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados del cliente",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ClienteDTO.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de Actualización",
                                    summary = "Actualiza datos del cliente",
                                    value = """
                                            {
                                              "rut": 12345678,
                                              "nombreCompleto": "Juan Pérez Actualizado",
                                              "email": "juan.nuevo@example.com",
                                              "telefono": "987654321",
                                              "activo": true,
                                              "fechaRegistro": "2026-06-20"
                                            }
                                            """
                            )
                    ))
            @Valid @RequestBody ClienteDTO dto) {
        dto.setId(id);
        ClienteDTO actualizado = clienteService.actualizarCliente(id, dto);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(clienteModelAssembler.toModel(actualizado));
    }

    // DELETE → ELIMINAR CLIENTE
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cliente",
            description = "Elimina un cliente por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar, tiene registros asociados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del cliente", required = true, example = "1")
            @PathVariable Integer id) {
        boolean eliminado = clienteService.eliminarClienteId(id);
        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}