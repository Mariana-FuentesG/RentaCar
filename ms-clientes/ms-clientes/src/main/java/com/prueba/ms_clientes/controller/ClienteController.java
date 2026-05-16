package com.prueba.ms_clientes.controller;

import com.prueba.ms_clientes.dto.ClienteDTO;
import com.prueba.ms_clientes.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ClienteController {

    @Autowired
    ClienteService clienteService;

    // GET /api/v1/clientes → lista todos los clientes
    @GetMapping("/clientes")
    public List<ClienteDTO> listarClientes() {
        return clienteService.obtenerClientes();
    }

    // GET → OBTENER CLIENTE POR ID
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> obtenerClientePorId(
            @PathVariable Integer id){
        ClienteDTO cliente = clienteService.ob

        if(cliente == null){

            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cliente);
    }





}
