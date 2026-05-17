package com.prueba.ms_pagos.client;

import com.prueba.ms_pagos.dto.ReservaDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "ms-reservas", url = "http://localhost:8083")
public interface ReservaClient {

    @GetMapping("/api/v1/reservas/{id}")
    ReservaDTO obtenerReservaPorId(
            @PathVariable Integer id);
}