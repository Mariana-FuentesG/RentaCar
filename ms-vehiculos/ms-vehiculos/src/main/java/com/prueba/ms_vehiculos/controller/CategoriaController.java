package com.prueba.ms_vehiculos.controller;

import com.prueba.ms_vehiculos.dto.CategoriaDTO;
import com.prueba.ms_vehiculos.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    @Autowired
    CategoriaService categoriaService;

    // GET → LISTAR TODAS LAS CATEGORIAS
    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> obtenerCategorias(){
        List<CategoriaDTO> categorias = categoriaService.obtenerCategorias();
        return ResponseEntity.ok(categorias);
    }

    // GET → BUSCAR CATEGORIA POR ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> obtenerCategoriaPorId(
            @PathVariable Integer id){
        CategoriaDTO categoria = categoriaService.obtenerCategoriaPorId(id);
        if(categoria == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categoria);
    }

    // POST → GUARDAR CATEGORIA
    @PostMapping
    public ResponseEntity<CategoriaDTO>
    guardarCategoria(@Valid @RequestBody CategoriaDTO dto){
        CategoriaDTO guardada = categoriaService.guardarCategoria(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(guardada);
    }

    // PUT → ACTUALIZAR CATEGORIA
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> actualizarCategoria(@PathVariable Integer id,
            @Valid @RequestBody CategoriaDTO dto){
        CategoriaDTO actualizada = categoriaService.actualizarCategoria(id, dto);
        if(actualizada == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizada);
    }

    // DELETE → ELIMINAR CATEGORIA
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Integer id){
        boolean eliminado = categoriaService.eliminarCategoria(id);
        if(!eliminado){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
