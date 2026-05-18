package com.prueba.ms_clientes.controller;

import com.prueba.ms_clientes.dto.ClienteDTO;
import com.prueba.ms_clientes.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    @Autowired
    ClienteService clienteService;

    // GET /api/v1/clientes → lista todos los clientes
    @GetMapping
    public List<ClienteDTO> listarClientes() {
        return clienteService.obtenerClientes();
    }

    // GET → OBTENER CLIENTE POR ID
    @GetMapping("{id}")
    public ResponseEntity<ClienteDTO> obtenerClientePorId(@PathVariable Integer id){
        ClienteDTO clientes = clienteService.obtenerClientePorId(id);
        if(clientes==null){return ResponseEntity.notFound().build();}
        return ResponseEntity.ok(clientes);
    }
    //GET → BUSCAR CLIENTE POR EMAIL
    @GetMapping("/email/{email}")
    public ResponseEntity<List<ClienteDTO>>obtenerClientePorEmail(@PathVariable String email){
        List <ClienteDTO> clientes = clienteService.obtenerClientesPorEmail(email);
        if(clientes.isEmpty()){return ResponseEntity.notFound().build();}
        return ResponseEntity.ok(clientes);
    }
    // GET → LISTAR CLIENTES ACTIVOS
    @GetMapping("/activos")
    public ResponseEntity<List<ClienteDTO>> obtenerClientesActivos() {
        List<ClienteDTO> clientes = clienteService.obtenerClientesActivos();
        if (clientes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(clientes);
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> guardar(@Valid @RequestBody ClienteDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clienteService.guardarCliente(dto));
    }
    //PUT → ACTUALIZAR CLIENTE
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> actualizar(@PathVariable Integer id,
            @Valid @RequestBody ClienteDTO dto){
        dto.setId(id);
        return ResponseEntity.ok(clienteService.actualizarCliente(id, dto));
    }
    //DELETE → ELIMINAR CLIENTE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id){
        clienteService.eliminarClienteId(id);
        return ResponseEntity.noContent().build();
    }

}
