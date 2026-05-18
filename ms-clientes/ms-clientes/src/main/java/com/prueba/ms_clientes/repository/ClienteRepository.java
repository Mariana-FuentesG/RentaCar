package com.prueba.ms_clientes.repository;

import com.prueba.ms_clientes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer>{
        List<Cliente> findByEmailContainingIgnoreCase(String email);

    // QUERY NATIVE → CLIENTES ACTIVOS
    @Query(value =
            "SELECT * FROM CLIENTES " +
                    "WHERE activo = true",
            nativeQuery = true)
    List<Cliente> obtenerClientesActivos();

}
