package com.prueba.ms_sucursales.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.ms_sucursales.assembler.RegionModelAssembler;
import com.prueba.ms_sucursales.dto.request.RegionRequestDTO;
import com.prueba.ms_sucursales.dto.response.RegionResponseDTO;
import com.prueba.ms_sucursales.exception.ResourceNotFoundException;
import com.prueba.ms_sucursales.model.Region;
import com.prueba.ms_sucursales.service.RegionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegionController.class)
class RegionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RegionService regionService;

    @MockitoBean
    private RegionModelAssembler assembler;

    private Region buildRegion() {
        Region region = new Region();
        region.setId(1);
        region.setNombre("Metropolitana");
        region.setCodigo("RM");
        region.setActiva(true);
        return region;
    }

    private RegionResponseDTO buildResponseDTO(Region region) {
        RegionResponseDTO dto = new RegionResponseDTO();
        dto.setId(region.getId());
        dto.setNombre(region.getNombre());
        dto.setCodigo(region.getCodigo());
        dto.setActiva(region.getActiva());
        return dto;
    }

    @Test
    void debeRetornarRegionPorId() throws Exception {
        Region region = buildRegion();
        when(regionService.obtenerRegionPorId(1)).thenReturn(region);
        when(assembler.toModel(region)).thenReturn(buildResponseDTO(region));

        mockMvc.perform(get("/api/v1/regiones/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value("RM"));
    }

    @Test
    void debeRetornar404SiRegionNoExiste() throws Exception {
        when(regionService.obtenerRegionPorId(99))
                .thenThrow(new ResourceNotFoundException("Región no encontrada con id: 99"));

        mockMvc.perform(get("/api/v1/regiones/{id}", 99))
                .andExpect(status().isNotFound());
    }

    @Test
    void debeCrearRegion() throws Exception {
        RegionRequestDTO request = new RegionRequestDTO("Valparaiso", "VA", true);
        Region region = buildRegion();
        when(regionService.guardarRegion(any(RegionRequestDTO.class))).thenReturn(region);
        when(assembler.toModel(region)).thenReturn(buildResponseDTO(region));

        mockMvc.perform(post("/api/v1/regiones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void debeRetornar400SiFaltaCampoObligatorio() throws Exception {
        RegionRequestDTO request = new RegionRequestDTO("", "VA", true);
        mockMvc.perform(post("/api/v1/regiones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void debeEliminarRegion() throws Exception {
        mockMvc.perform(delete("/api/v1/regiones/{id}", 1))
                .andExpect(status().isNoContent());
    }
}
