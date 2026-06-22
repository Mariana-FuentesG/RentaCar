package com.prueba.ms_vehiculos.controller;

import com.prueba.ms_vehiculos.assembler.CategoriaModelAssembler;
import com.prueba.ms_vehiculos.dto.CategoriaDTO;
import com.prueba.ms_vehiculos.service.CategoriaService;
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
@RequestMapping("/api/v1/categorias")
@Tag(name = "Categorías", description = "Gestión de categorías de vehículos de RentaCar")
public class CategoriaController {

    @Autowired
    CategoriaService categoriaService;

    @Autowired
    CategoriaModelAssembler categoriaModelAssembler;

    // GET → LISTAR TODAS LAS CATEGORIAS
    @GetMapping
    @Operation(summary = "Obtener todas las categorías",
            description = "Retorna el listado completo de categorías con enlaces HATEOAS")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoriaDTO.class)))
    })
    public ResponseEntity<CollectionModel<EntityModel<CategoriaDTO>>> obtenerCategorias() {
        List<EntityModel<CategoriaDTO>> categorias = categoriaService.obtenerCategorias()
                .stream()
                .map(categoriaModelAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<CategoriaDTO>> coleccion = CollectionModel.of(
                categorias,
                linkTo(methodOn(CategoriaController.class).obtenerCategorias()).withSelfRel()
        );

        return ResponseEntity.ok(coleccion);
    }

    // GET → BUSCAR CATEGORIA POR ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría por ID",
            description = "Retorna una categoría específica con enlaces HATEOAS")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoriaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    public ResponseEntity<EntityModel<CategoriaDTO>> obtenerCategoriaPorId(
            @Parameter(description = "ID de la categoría", required = true, example = "1")
            @PathVariable Integer id) {
        CategoriaDTO categoria = categoriaService.obtenerCategoriaPorId(id);
        if (categoria == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categoriaModelAssembler.toModel(categoria));
    }

    // POST → GUARDAR CATEGORIA
    @PostMapping
    @Operation(summary = "Crear nueva categoría",
            description = "Registra una nueva categoría de vehículos")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoriaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<EntityModel<CategoriaDTO>> guardarCategoria(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la categoría a crear",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoriaDTO.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de Categoría",
                                    summary = "Nueva categoría de vehículo",
                                    value = """
                                            {
                                              "nombre": "SUV",
                                              "capacidadPasajeros": 5,
                                              "activa": true,
                                              "fechaCreacion": "2026-06-20",
                                              "precioBase": 80000.0
                                            }
                                            """
                            )
                    ))
            @Valid @RequestBody CategoriaDTO dto) {
        CategoriaDTO guardada = categoriaService.guardarCategoria(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoriaModelAssembler.toModel(guardada));
    }

    // PUT → ACTUALIZAR CATEGORIA
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar categoría existente",
            description = "Actualiza los datos de una categoría por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoriaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<EntityModel<CategoriaDTO>> actualizarCategoria(
            @Parameter(description = "ID de la categoría", required = true, example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados de la categoría",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoriaDTO.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de Actualización",
                                    summary = "Actualiza datos de la categoría",
                                    value = """
                                            {
                                              "nombre": "SUV Premium",
                                              "capacidadPasajeros": 7,
                                              "activa": true,
                                              "fechaCreacion": "2026-06-20",
                                              "precioBase": 100000.0
                                            }
                                            """
                            )
                    ))
            @Valid @RequestBody CategoriaDTO dto) {
        CategoriaDTO actualizada = categoriaService.actualizarCategoria(id, dto);
        if (actualizada == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categoriaModelAssembler.toModel(actualizada));
    }

    // DELETE → ELIMINAR CATEGORIA
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoría",
            description = "Elimina una categoría por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoría eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar, tiene vehículos asociados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> eliminarCategoria(
            @Parameter(description = "ID de la categoría", required = true, example = "1")
            @PathVariable Integer id) {
        boolean eliminado = categoriaService.eliminarCategoria(id);
        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}