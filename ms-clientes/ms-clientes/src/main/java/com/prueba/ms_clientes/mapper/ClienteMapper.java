package com.prueba.ms_clientes.mapper;

import com.prueba.ms_clientes.dto.ClienteDTO;
import com.prueba.ms_clientes.model.Cliente;

public class ClienteMapper {
    public static ClienteDTO toDTO(Cliente cliente) {
        return new ClienteDTO(
                cliente.getId(),
                cliente.getRut(),
                cliente.getNombreCompleto(),
                cliente.getEmail(),
                cliente.getTelefono(),
                cliente.getActivo(),
                cliente.getFechaRegistro(),
                null //la lista de direcciones del cliente
        );
    }

    public static Cliente toEntity(ClienteDTO dto) {
        return new Cliente(
                dto.getId(),
                dto.getRut(),
                dto.getNombreCompleto(),
                dto.getEmail(),
                dto.getTelefono(),
                dto.getActivo(),
                dto.getFechaRegistro(),
                null //la lista de direcciones del cliente
        );
    }
}

