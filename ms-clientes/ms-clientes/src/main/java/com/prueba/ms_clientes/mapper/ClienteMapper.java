package com.prueba.ms_clientes.mapper;

import com.prueba.ms_clientes.dto.ClienteDTO;
import com.prueba.ms_clientes.dto.DireccionDTO;
import com.prueba.ms_clientes.model.Cliente;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.stream.Collectors;

public class ClienteMapper {
    public static ClienteDTO toDTO(Cliente cliente) {
        List Direcciones DireccionDTO = cliente.getDirecciones()
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
                cliente.



        )
    }
}
