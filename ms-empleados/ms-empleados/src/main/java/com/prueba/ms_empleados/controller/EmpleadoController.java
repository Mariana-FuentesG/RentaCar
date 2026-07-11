package com.prueba.ms_empleados.controller;

import com.prueba.ms_empleados.dto.EmpleadoDTO;
import com.prueba.ms_empleados.service.EmpleadoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    // GET → LISTAR TODOS LOS EMPLEADOS
    @GetMapping
    public ResponseEntity<List<EmpleadoDTO>> obtenerEmpleados() {
        List<EmpleadoDTO> empleados = empleadoService.obtenerEmpleados();
        return ResponseEntity.ok(empleados);
    }

    // QUERY NATIVE
    // LISTAR EMPLEADOS ACTIVOS CONTRATADOS EN UN AÑO DETERMINADO
    @GetMapping("/activos")
    public ResponseEntity<List<EmpleadoDTO>>
    obtenerEmpleadosActivosPorAnio(@RequestParam Integer anio) {
        List<EmpleadoDTO> empleados = empleadoService
                .obtenerEmpleadosActivosPorAnio(anio);
        return ResponseEntity.ok(empleados);
    }

    // GET → OBTENER EMPLEADO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoDTO> obtenerEmpleadoPorId(@PathVariable Integer id) {
        EmpleadoDTO empleado = empleadoService.obtenerEmpleadoPorId(id);
        if (empleado == null) {return ResponseEntity.notFound().build();
        }return ResponseEntity.ok(empleado);
    }

    // POST → CREAR NUEVO EMPLEADO
    @PostMapping
    public ResponseEntity<EmpleadoDTO> guardarEmpleado(@Valid @RequestBody EmpleadoDTO dto) {
        EmpleadoDTO guardado = empleadoService.guardarEmpleado(dto);
        if (guardado == null) {return ResponseEntity.badRequest().build();
        }return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(guardado);
    }

    // PUT → ACTUALIZAR EMPLEADO
    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoDTO>
    actualizarEmpleado(@PathVariable Integer id, @Valid @RequestBody EmpleadoDTO dto) {
        EmpleadoDTO actualizado = empleadoService.actualizarEmpleado(id, dto);
        if (actualizado == null) {return ResponseEntity.notFound().build();
        }return ResponseEntity.ok(actualizado);
    }

    // DELETE → ELIMINAR EMPLEADO
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEmpleado(@PathVariable Integer id) {
        boolean eliminado = empleadoService.eliminarEmpleado(id);
        if (!eliminado) {return ResponseEntity.notFound().build();
        }return ResponseEntity.noContent().build();
    }
}
