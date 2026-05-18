package com.prueba.ms_reservas.client;

import com.prueba.ms_reservas.dto.VehiculoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-vehiculos", url = "http://localhost:8082/api/v1/vehiculos"
)
public interface VehiculoClient {

    @GetMapping("/{id}")
    VehiculoDTO obtenerVehiculoPorId(
            @PathVariable("id") Integer id);
}