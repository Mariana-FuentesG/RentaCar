package com.prueba.ms_clientes.mapper;

import com.prueba.ms_clientes.dto.ClienteDTO;
import com.prueba.ms_clientes.dto.ClienteRequestDTO;
import com.prueba.ms_clientes.dto.DireccionDTO;
import com.prueba.ms_clientes.model.Cliente;

import java.util.List;
import java.util.stream.Collectors;

public class ClienteMapper {
    public static ClienteDTO toDTO(Cliente cliente) {
        List <DireccionDTO>  direcccionDTO = cliente.getDirecciones()
                .stream()
                .map(direccion -> new DireccionDTO(
                        direccion.getId(),
                        direccion.getCalle(),
                        direccion.getComuna(),
                        direccion.getNumeroCasa(),
                        direccion.getCodigoPostal(),
                        direccion.getEstado(),
                        direccion.getFechaRegistro(),
                        cliente.getId(),
                        cliente.getNombreCompleto()
                ))
                .collect(Collectors.toList());

        return new ClienteDTO(
                cliente.getId(),
                cliente.getNombreCompleto(),
                cliente.getRut(),
                cliente.getEmail(),
                cliente.getTelefono(),
                cliente.getActivo(),
                cliente.getFechaRegistro(),
                direcccionDTO
        );
    }

    public static Cliente toEntity(ClienteRequestDTO dto) {
        return Cliente.builder()
                .nombreCompleto(dto.getNombreCompleto())
                .rut(dto.getRut())
                .email(dto.getEmail())
                .telefono(dto.getTelefono())
                .activo(dto.getActivo())
                .fechaRegistro(dto.getFechaRegistro())
                .build();
    }

}
