package com.prueba.ms_reservas.controller;

import com.prueba.ms_reservas.dto.EstadoReservaDTO;
import com.prueba.ms_reservas.service.EstadoReservaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/estados-reserva")
@Tag(name="",description = "")
public class EstadoReservaController {

    @Autowired
    EstadoReservaService estadoReservaService;

    // GET → LISTAR TODOS LOS ESTADOS
    @GetMapping
    public ResponseEntity<List<EstadoReservaDTO>>
    obtenerEstadosReserva(){
        List<EstadoReservaDTO> estados = estadoReservaService.obtenerEstadosReserva();
        return ResponseEntity.ok(estados);
    }

    // GET → BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<EstadoReservaDTO>
    obtenerEstadoReservaPorId(@PathVariable Integer id){
        EstadoReservaDTO estado = estadoReservaService
                        .obtenerEstadoReservaPorId(id);
        if(estado == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(estado);
    }

    // POST → GUARDAR
    @PostMapping
    public ResponseEntity<EstadoReservaDTO>
    guardarEstadoReserva(@Valid @RequestBody EstadoReservaDTO dto){
        EstadoReservaDTO guardado = estadoReservaService.guardarEstadoReserva(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(guardado);
    }

    // PUT → ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<EstadoReservaDTO> actualizarEstadoReserva(@PathVariable Integer id,
            @Valid @RequestBody EstadoReservaDTO dto){
        EstadoReservaDTO actualizado = estadoReservaService
                        .actualizarEstadoReserva(id, dto);
        if(actualizado == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    // DELETE → ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEstadoReserva(@PathVariable Integer id){
        boolean eliminado = estadoReservaService
                        .eliminarEstadoReserva(id);
        if(!eliminado){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}