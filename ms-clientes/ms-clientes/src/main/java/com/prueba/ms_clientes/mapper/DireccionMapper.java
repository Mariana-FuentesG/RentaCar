package com.prueba.ms_clientes.mapper;

import com.prueba.ms_clientes.dto.DireccionDTO;
import com.prueba.ms_clientes.model.Cliente;
import com.prueba.ms_clientes.model.Direccion;

public class DireccionMapper {

    // ENTITY → DTO
    public static DireccionDTO toDTO(Direccion direccion){
        return new DireccionDTO(
                direccion.getId(),
                direccion.getCalle(),
                direccion.getNumeroCasa(),
                direccion.getComuna(),
                direccion.getCiudad(),
                direccion.getCodigoPostal(),
                direccion.getEstado(),
                direccion.getFechaRegistro(),

                // DATOS CLIENTE RELACIONADO
                direccion.getCliente().getId(),
                direccion.getCliente().getNombreCompleto()
        );
    }

    // DTO → ENTITY
    public static Direccion toEntity(DireccionDTO dto){

        Direccion direccion = new Direccion();
        direccion.setId(dto.getId());
        direccion.setCalle(dto.getCalle());
        direccion.setNumeroCasa(dto.getNumeroCasa());
        direccion.setComuna(dto.getComuna());
        direccion.setCiudad(dto.getCiudad());
        direccion.setCodigoPostal(dto.getCodigoPostal());
        direccion.setEstado(dto.getEstado());
        direccion.setFechaRegistro(dto.getFechaRegistro());

        // RELACION MANY TO ONE CON CLIENTE
        Cliente cliente = new Cliente();
        cliente.setId(dto.getClienteId());

        direccion.setCliente(cliente);

        return direccion;
    }
}