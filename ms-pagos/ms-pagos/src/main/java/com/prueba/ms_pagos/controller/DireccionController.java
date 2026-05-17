package com.prueba.ms_pagos.controller;

import com.prueba.ms_clientes.dto.DireccionDTO;
import com.prueba.ms_clientes.service.DireccionService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/direcciones")

public class DireccionController {

    @Autowired
    DireccionService direccionService;

    // GET → LISTAR TODAS LAS DIRECCIONES
    @GetMapping
    public ResponseEntity<List<DireccionDTO>> obtenerDirecciones(){
        return ResponseEntity.ok(
                direccionService.obtenerDirecciones()
        );
    }

    // GET → OBTENER DIRECCION POR ID
    @GetMapping("/{id}")
    public ResponseEntity<DireccionDTO> obtenerDireccionPorId(
            @PathVariable Integer id){
        DireccionDTO direccion =
                direccionService.obtenerDireccionPorId(id);
        if(direccion == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(direccion);
    }

    // GET → BUSCAR DIRECCIONES POR COMUNA

    @GetMapping("/comuna/{}")
    public ResponseEntity<List<DireccionDTO>> buscarPorComuna(
            @PathVariable String comuna){
        List<DireccionDTO> direcciones =
                direccionService.buscarPorComuna(comuna);
        if(direcciones.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(direcciones);
    }

    // POST → CREAR DIRECCION
    @PostMapping
    public ResponseEntity<DireccionDTO> guardar(
            @Valid @RequestBody DireccionDTO dto){
        DireccionDTO direccionCreada = direccionService.guardarDireccion(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(direccionCreada);
    }

    // PUT → ACTUALIZAR DIRECCION
    @PutMapping("/{id}")
    public ResponseEntity<DireccionDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody DireccionDTO dto){
        DireccionDTO direccionActualizada =
                direccionService.actualizarDireccion(id, dto);
        if(direccionActualizada == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(direccionActualizada);
    }

    // DELETE → ELIMINAR DIRECCION
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Integer id){
        boolean eliminado =
                direccionService.eliminarDireccion(id);
        if(!eliminado){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}