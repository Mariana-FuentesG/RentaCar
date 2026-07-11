package com.prueba.ms_vehiculos.assembler;

import com.prueba.ms_vehiculos.controller.CategoriaController;
import com.prueba.ms_vehiculos.dto.CategoriaDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Convierte un CategoriaDTO en un EntityModel<CategoriaDTO> agregando
 * los enlaces HATEOAS (_links) de navegación: self, colección,
 * actualizar y eliminar.
 */
@Component
public class CategoriaModelAssembler
        implements RepresentationModelAssembler<CategoriaDTO, EntityModel<CategoriaDTO>> {

    @Override
    public @NonNull EntityModel<CategoriaDTO> toModel(@NonNull CategoriaDTO categoria) {
        return EntityModel.of(categoria,
                // ENLACE SELF → ESTA MISMA CATEGORIA
                linkTo(methodOn(CategoriaController.class)
                        .obtenerCategoriaPorId(categoria.getId())).withSelfRel(),

                // ENLACE → COLECCIÓN COMPLETA DE CATEGORIAS
                linkTo(methodOn(CategoriaController.class)
                        .obtenerCategorias()).withRel("categorias"),

                // ENLACE → ACTUALIZAR ESTA CATEGORIA (PUT)
                linkTo(methodOn(CategoriaController.class)
                        .actualizarCategoria(categoria.getId(), null)).withRel("actualizar"),

                // ENLACE → ELIMINAR ESTA CATEGORIA (DELETE)
                linkTo(methodOn(CategoriaController.class)
                        .eliminarCategoria(categoria.getId())).withRel("eliminar")
        );
    }
}