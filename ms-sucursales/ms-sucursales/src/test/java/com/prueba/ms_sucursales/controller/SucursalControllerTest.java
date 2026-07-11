package com.prueba.ms_sucursales.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.ms_sucursales.assembler.SucursalModelAssembler;
import com.prueba.ms_sucursales.dto.request.SucursalRequestDTO;
import com.prueba.ms_sucursales.dto.response.SucursalResponseDTO;
import com.prueba.ms_sucursales.exception.ResourceNotFoundException;
import com.prueba.ms_sucursales.model.Region;
import com.prueba.ms_sucursales.model.Sucursal;
import com.prueba.ms_sucursales.service.SucursalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SucursalController.class)
class SucursalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SucursalService sucursalService;

    @MockitoBean
    private SucursalModelAssembler assembler;

    private Sucursal buildSucursal() {
        Region region = new Region();
        region.setId(1);
        region.setNombre("Metropolitana");
        region.setCodigo("RM");
        region.setActiva(true);

        Sucursal sucursal = new Sucursal();
        sucursal.setId(1);
        sucursal.setNombre("Sucursal Santiago");
        sucursal.setDireccion("Alameda 123");
        sucursal.setTelefono("987654321");
        sucursal.setCiudad("Santiago");
        sucursal.setActiva(true);
        sucursal.setCantidadVehiculos(20);
        sucursal.setRegion(region);
        return sucursal;
    }

    private SucursalResponseDTO buildResponseDTO(Sucursal sucursal) {
        SucursalResponseDTO dto = new SucursalResponseDTO();
        dto.setId(sucursal.getId());
        dto.setNombre(sucursal.getNombre());
        dto.setDireccion(sucursal.getDireccion());
        dto.setTelefono(sucursal.getTelefono());
        dto.setCiudad(sucursal.getCiudad());
        dto.setActiva(sucursal.getActiva());
        dto.setCantidadVehiculos(sucursal.getCantidadVehiculos());
        dto.setRegionId(sucursal.getRegion().getId());
        dto.setRegionNombre(sucursal.getRegion().getNombre());
        return dto;
    }

    @Test
    void debeListarSucursales() throws Exception {
        Sucursal sucursal = buildSucursal();
        when(sucursalService.obtenerSucursales()).thenReturn(List.of(sucursal));
        when(assembler.toCollectionModel(any())).thenAnswer(inv ->
                org.springframework.hateoas.CollectionModel.of(List.of(buildResponseDTO(sucursal))));

        mockMvc.perform(get("/api/v1/sucursales"))
                .andExpect(status().isOk());
    }

    @Test
    void debeRetornarSucursalPorId() throws Exception {
        Sucursal sucursal = buildSucursal();
        when(sucursalService.obtenerSucursalPorId(1)).thenReturn(sucursal);
        when(assembler.toModel(sucursal)).thenReturn(buildResponseDTO(sucursal));

        mockMvc.perform(get("/api/v1/sucursales/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Sucursal Santiago"));
    }

    @Test
    void debeRetornar404SiSucursalNoExiste() throws Exception {
        when(sucursalService.obtenerSucursalPorId(99))
                .thenThrow(new ResourceNotFoundException("Sucursal no encontrada con id: 99"));

        mockMvc.perform(get("/api/v1/sucursales/{id}", 99))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void debeCrearSucursal() throws Exception {
        SucursalRequestDTO request = new SucursalRequestDTO(
                "Sucursal Nueva", "Calle 1", "911111111", "Santiago", true, 5, 1);
        Sucursal sucursal = buildSucursal();
        when(sucursalService.guardarSucursal(any(SucursalRequestDTO.class))).thenReturn(sucursal);
        when(assembler.toModel(sucursal)).thenReturn(buildResponseDTO(sucursal));

        mockMvc.perform(post("/api/v1/sucursales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Sucursal Santiago"));
    }

    @Test
    void debeRetornar400SiFaltaCampoObligatorio() throws Exception {
        SucursalRequestDTO request = new SucursalRequestDTO(
                "", "Calle 1", "911111111", "Santiago", true, 5, 1);

        mockMvc.perform(post("/api/v1/sucursales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void debeEliminarSucursal() throws Exception {
        mockMvc.perform(delete("/api/v1/sucursales/{id}", 1))
                .andExpect(status().isNoContent());
    }
}
