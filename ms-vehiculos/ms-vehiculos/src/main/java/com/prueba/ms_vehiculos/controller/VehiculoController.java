package com.prueba.ms_vehiculos.controller;

import com.prueba.ms_vehiculos.dto.VehiculoDTO;
import com.prueba.ms_vehiculos.service.VehiculoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehiculos")
public class VehiculoController {

    @Autowired
    VehiculoService vehiculoService;

    // GET → LISTAR TODOS LOS VEHICULOS
    @GetMapping
    public ResponseEntity<List<VehiculoDTO>> obtenerVehiculos(){
        List<VehiculoDTO> vehiculos = vehiculoService.obtenerVehiculos();
        return ResponseEntity.ok(vehiculos);
    }

    // GET → BUSCAR VEHICULO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<VehiculoDTO> obtenerVehiculoPorId(
            @PathVariable Integer id){
        VehiculoDTO vehiculo = vehiculoService.obtenerVehiculoPorId(id);
        if(vehiculo == null){
            return ResponseEntity.
                    notFound()
                    .build();
        }
        return ResponseEntity.ok(vehiculo);
    }

    // POST → GUARDAR VEHICULO
    @PostMapping
    public ResponseEntity<VehiculoDTO>
    guardarVehiculo(@Valid @RequestBody VehiculoDTO dto){
        VehiculoDTO guardado = vehiculoService.guardarVehiculo(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(guardado);
    }

    // PUT → ACTUALIZAR VEHICULO
    @PutMapping("/{id}")
    public ResponseEntity<VehiculoDTO>
    actualizarVehiculo(@PathVariable Integer id,
            @Valid @RequestBody VehiculoDTO dto) {
        VehiculoDTO actualizado = vehiculoService.actualizarVehiculo(id, dto);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    // DELETE → ELIMINAR VEHICULO
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVehiculo(@PathVariable Integer id){
        boolean eliminado =
                vehiculoService.eliminarVehiculo(id);
        if(!eliminado){
            return ResponseEntity
                    .notFound()
                    .build();
        }
        return ResponseEntity
                .noContent()
                .build();
    }

    // QUERY METHOD OBLIGATORIO
    @GetMapping("/buscar")
    public ResponseEntity<List<VehiculoDTO>> obtenerVehiculosDisponiblesPorPrecio(
            @RequestParam Double precio){
        List<VehiculoDTO> vehiculos = vehiculoService
                        .obtenerVehiculosDisponiblesPorPrecio(precio);
        return ResponseEntity.ok(vehiculos);
    }
}