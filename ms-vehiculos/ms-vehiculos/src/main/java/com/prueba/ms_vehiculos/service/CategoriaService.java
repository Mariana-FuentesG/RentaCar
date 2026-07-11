package com.prueba.ms_vehiculos.service;

import com.prueba.ms_vehiculos.dto.CategoriaDTO;
import com.prueba.ms_vehiculos.mapper.CategoriaMapper;
import com.prueba.ms_vehiculos.model.Categoria;
import com.prueba.ms_vehiculos.repository.CategoriaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    @Autowired
    CategoriaRepository categoriaRepository;

    // GET → LISTAR TODAS LAS CATEGORIAS
    public List<CategoriaDTO> obtenerCategorias(){
        return categoriaRepository.findAll()
                .stream()
                .map(CategoriaMapper::toDTO)
                .collect(Collectors.toList());
    }

    // POST → GUARDAR CATEGORIA
    public CategoriaDTO guardarCategoria(CategoriaDTO dto){
        Categoria categoria = CategoriaMapper.toEntity(dto);
        Categoria guardada = categoriaRepository.save(categoria);
        return CategoriaMapper.toDTO(guardada);
    }

    // GET → BUSCAR CATEGORIA POR ID
    public CategoriaDTO obtenerCategoriaPorId(Integer id){
        Categoria categoria = categoriaRepository.findById(id)
                        .orElse(null);
        if(categoria == null){return null;}
        return CategoriaMapper.toDTO(categoria);
    }

    // PUT → ACTUALIZAR CATEGORIA
    public CategoriaDTO actualizarCategoria(Integer id, CategoriaDTO dto){
        try {
            Categoria categoria = categoriaRepository.findById(id)
                            .orElse(null);
            if(categoria == null){return null;
            }
            categoria.setNombre(dto.getNombre());
            categoria.setCapacidadPasajeros(dto.getCapacidadPasajeros());
            categoria.setActiva(dto.getActiva());
            categoria.setFechaCreacion(dto.getFechaCreacion());
            categoria.setPrecioBase(dto.getPrecioBase());
            Categoria actualizada = categoriaRepository.save(categoria);
            return CategoriaMapper.toDTO(actualizada);
        }catch (Exception e){
            throw new RuntimeException(
                    "Error al actualizar categoría");
        }
    }

    // DELETE → ELIMINAR CATEGORIA
    @Transactional
    public boolean eliminarCategoria(Integer id){
        try {Categoria eliminar = categoriaRepository.findById(id)
                            .orElse(null);
            if(eliminar == null){return false;}
            categoriaRepository.delete(eliminar);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}