package com.prueba.ms_sucursales.controller;

import com.prueba.ms_sucursales.dto.SucursalDTO;
import com.prueba.ms_sucursales.service.SucursalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sucursales")
public class SucursalController {

    @Autowired
    private SucursalService sucursalService;

    // GET → LISTAR TODAS LAS SUCURSALES
    @GetMapping
    public ResponseEntity<List<SucursalDTO>> obtenerSucursales() {
        return ResponseEntity.ok(
                sucursalService.obtenerSucursales()
        );
    }

    @GetMapping("/operativas")
    public ResponseEntity<List<SucursalDTO>> obtenerSucursalesOperativas() {
        List<SucursalDTO> sucursales = sucursalService
                        .obtenerSucursalesOperativas();
        return ResponseEntity.ok(sucursales);
    }

    // GET → BUSCAR SUCURSAL POR ID
    @GetMapping("/{id}")
    public ResponseEntity<SucursalDTO>
    obtenerSucursalPorId(@PathVariable Integer id) {
        SucursalDTO sucursal = sucursalService.obtenerSucursalPorId(id);
        if (sucursal == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(sucursal);
    }

    // POST → CREAR NUEVA SUCURSAL
    @PostMapping
    public ResponseEntity<SucursalDTO>
    guardarSucursal(@Valid @RequestBody SucursalDTO dto) {
        SucursalDTO guardada = sucursalService.guardarSucursal(dto);
        if (guardada == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(guardada);
    }

    // PUT → ACTUALIZAR SUCURSAL
    @PutMapping("/{id}")
    public ResponseEntity<SucursalDTO>
    actualizarSucursal(@PathVariable Integer id, @Valid @RequestBody SucursalDTO dto) {
        SucursalDTO actualizada =
                sucursalService.actualizarSucursal(id, dto);
        if (actualizada == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizada);
    }

    // DELETE → ELIMINAR SUCURSAL
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSucursal(@PathVariable Integer id) {
        boolean eliminada = sucursalService.eliminarSucursal(id);
        if (!eliminada) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
