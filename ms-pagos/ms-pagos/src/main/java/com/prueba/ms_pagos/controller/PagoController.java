package com.prueba.ms_pagos.controller;

import com.prueba.ms_pagos.dto.PagoDTO;
import com.prueba.ms_pagos.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pagos")
public class PagoController {

    @Autowired
    PagoService pagoService;

    // GET → LISTAR TODOS LOS PAGOS
    @GetMapping
    public ResponseEntity<List<PagoDTO>> obtenerPagos(){
        List<PagoDTO> pagos = pagoService.obtenerPagos();
        return ResponseEntity.ok(pagos);
    }

    // GET → BUSCAR PAGO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<PagoDTO> obtenerPagoPorId(@PathVariable Integer id){
        PagoDTO pago = pagoService.obtenerPagoPorId(id);
        if(pago == null){return ResponseEntity.notFound().build();
        }return ResponseEntity.ok(pago);
    }

    // POST → GUARDAR PAGO
    @PostMapping
    public ResponseEntity<PagoDTO> guardarPago(@Valid @RequestBody PagoDTO dto){
        PagoDTO guardado = pagoService.guardarPago(dto);
        if(guardado == null){return ResponseEntity.badRequest().build();}
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(guardado);
    }

    // PUT → ACTUALIZAR PAGO
    @PutMapping("/{id}")
    public ResponseEntity<PagoDTO> actualizarPago(@PathVariable Integer id,
            @Valid @RequestBody PagoDTO dto){
        PagoDTO actualizado = pagoService.actualizarPago(id,dto);
        if(actualizado == null){return ResponseEntity.notFound().build();}
        return ResponseEntity.ok(actualizado);
    }

    // DELETE → ELIMINAR PAGO
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable Integer id){
        boolean eliminado = pagoService.eliminarPago(id);
        if(!eliminado){return ResponseEntity.notFound().build();}
        return ResponseEntity
                .noContent()
                .build();
    }

    // JPQL → BUSCAR PAGOS POR MONTO
    @GetMapping("/buscar")
    public ResponseEntity<List<PagoDTO>> buscarPagosPorMonto(@RequestParam Double minimo,
            @RequestParam Double maximo){
        List<PagoDTO> pagos = pagoService
                .buscarPagosPorMonto(minimo,maximo);
        return ResponseEntity.ok(pagos);
    }
}