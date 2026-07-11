package com.prueba.ms_clientes.service;

import com.prueba.ms_clientes.dto.ClienteDTO;
import com.prueba.ms_clientes.mapper.ClienteMapper;
import com.prueba.ms_clientes.model.Cliente;
import com.prueba.ms_clientes.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    ClienteRepository clienteRepository;

    // GET /api/v1/clientes → retorna todos los clientes como lista de DTO
    public List<ClienteDTO> obtenerClientes() {
        return clienteRepository.findAll()
                .stream()
                .map(ClienteMapper::toDTO)
                .collect(Collectors.toList());
    }

    // POST /api/v1/clientes → guarda un nuevo cliente recibido como DTO
    public ClienteDTO guardarCliente(ClienteDTO dto) {
        Cliente cliente = ClienteMapper.toEntity(dto);
        Cliente guardado = clienteRepository.save(cliente);
        return ClienteMapper.toDTO(guardado);
    }

    //GET- buscar cliente por EMAil
    public List<ClienteDTO> obtenerClientesPorEmail(String email) {
        return clienteRepository.findByEmailContainingIgnoreCase(email)
                .stream()
                .map(ClienteMapper::toDTO)
                .collect(Collectors.toList());
    }
    //GET → OBTENER CLIENTE POR ID
    public ClienteDTO obtenerClientePorId(Integer id){
        Cliente cliente = clienteRepository.findById(id)
                .orElse(null);
        if(cliente == null){return null;
        }
        return ClienteMapper.toDTO(cliente);
    }

    // PUT actualizar cliente
    public ClienteDTO actualizarCliente(Integer id, ClienteDTO dto){
        try {
            Cliente cliente = clienteRepository.findById(id)
                    .orElse(null);
            if (cliente == null) {
                return null;
            }
            cliente.setNombreCompleto(dto.getNombreCompleto());
            cliente.setRut(dto.getRut());
            cliente.setEmail(dto.getEmail());
            cliente.setTelefono(dto.getTelefono());
            cliente.setActivo(dto.getActivo());
            cliente.setFechaRegistro(dto.getFechaRegistro());

            Cliente actualizado = clienteRepository.save(cliente);
            return ClienteMapper.toDTO(actualizado);
        }
        catch (Exception e){
            throw new RuntimeException("Error al actualizar cliente");
        }
    }

    @Transactional
    public boolean eliminarClienteId(Integer id){
        Cliente eliminar = clienteRepository.findById(id)
                            .orElse(null);
            if(eliminar == null){
                return false;
            }clienteRepository.delete(eliminar);
            return true;

    }

    // QUERY NATIVE → CLIENTES ACTIVOS
    public List<ClienteDTO> obtenerClientesActivos(){
        return clienteRepository
                .obtenerClientesActivos()
                .stream()
                .map(ClienteMapper::toDTO)
                .collect(Collectors.toList());
    }

}
