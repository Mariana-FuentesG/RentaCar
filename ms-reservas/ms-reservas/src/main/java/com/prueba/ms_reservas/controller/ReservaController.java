package com.prueba.ms_reservas.controller;

import com.prueba.ms_reservas.dto.ReservaDTO;
import com.prueba.ms_reservas.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reservas")
public class ReservaController {

    @Autowired
    ReservaService reservaService;

    // GET → LISTAR TODAS LAS RESERVAS
    @GetMapping
    public ResponseEntity<List<ReservaDTO>> obtenerReservas(){
        List<ReservaDTO> reservas = reservaService.obtenerReservas();
        return ResponseEntity.ok(reservas);
    }

    // GET → BUSCAR RESERVA POR ID
    @GetMapping("/{id}")
    public ResponseEntity<ReservaDTO> obtenerReservaPorId(
            @PathVariable Integer id){
        ReservaDTO reserva = reservaService.obtenerReservaPorId(id);
        if(reserva == null){
            return ResponseEntity
                    .notFound()
                    .build();
        }
        return ResponseEntity.ok(
                reserva);
    }

    // POST → GUARDAR RESERVA
    @PostMapping
    public ResponseEntity<ReservaDTO>
    guardarReserva(@Valid @RequestBody
            ReservaDTO dto){
        ReservaDTO reservaGuardada = reservaService.guardarReserva(dto);
        if (reservaGuardada == null){

            return ResponseEntity
                    .notFound()
                    .build();

        }

        return ResponseEntity
                .status(
                        HttpStatus.CREATED)
                .body(
                        reservaGuardada);
    }

    // PUT → ACTUALIZAR RESERVA
    @PutMapping("/{id}")
    public ResponseEntity<ReservaDTO> actualizarReserva(
            @PathVariable Integer id,
            @Valid
            @RequestBody
            ReservaDTO dto){

        ReservaDTO reservaActualizada =
                reservaService
                        .actualizarReserva(
                                id, dto);

        if(reservaActualizada == null){

            return ResponseEntity
                    .notFound()
                    .build();
        }
        return ResponseEntity
                .ok
                (reservaActualizada);
    }

    // GET → BUSCAR RESERVAS DESDE FECHA

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<ReservaDTO>>
    buscarReservasDesdeFecha(@PathVariable LocalDate fecha){
        List<ReservaDTO> reservas = reservaService
                        .buscarReservasDesdeFecha(fecha);
        if(reservas.isEmpty()){
            return ResponseEntity
                    .notFound()
                    .build();
        }
        return ResponseEntity
                .ok(reservas);
    }

    // DELETE → ELIMINAR RESERVA
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReserva(@PathVariable Integer id){
        boolean eliminado = reservaService.eliminarReserva(id);
        if(!eliminado){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}