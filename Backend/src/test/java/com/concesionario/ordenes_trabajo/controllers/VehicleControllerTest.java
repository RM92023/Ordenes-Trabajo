package com.concesionario.ordenes_trabajo.controllers;

import com.concesionario.ordenes_trabajo.dtos.VehicleDTO;
import com.concesionario.ordenes_trabajo.models.Vehicle;
import com.concesionario.ordenes_trabajo.services.VehicleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class VehicleControllerTest {

    private MockMvc mockMvc;
    private VehicleService vehicleService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        vehicleService = mock(VehicleService.class);
        VehicleController controller = new VehicleController(vehicleService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createVehicle_shouldReturnOk() throws Exception {
        VehicleDTO dto = new VehicleDTO("ABC123", "Toyota", "Corolla", 2021);
        Vehicle vehicle = new Vehicle(1L, "ABC123", "Toyota", "Corolla", 2021, null);

        when(vehicleService.createVehicle(any())).thenReturn(vehicle);

        mockMvc.perform(post("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.licensePlate").value("ABC123"));
    }

    @Test
    void getAll_shouldReturnList() throws Exception {
        Vehicle v1 = new Vehicle(1L, "AAA111", "Ford", "Fiesta", 2019, null);
        Vehicle v2 = new Vehicle(2L, "BBB222", "Chevy", "Spark", 2020, null);

        when(vehicleService.getAllVehicles()).thenReturn(List.of(v1, v2));

        mockMvc.perform(get("/api/vehicles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].licensePlate").value("AAA111"));
    }

    @Test
    void getByLicensePlate_shouldReturnVehicle() throws Exception {
        Vehicle vehicle = new Vehicle(1L, "XYZ789", "Mazda", "CX-5", 2023, null);
        when(vehicleService.getVehicleByLicensePlate("XYZ789")).thenReturn(vehicle);

        mockMvc.perform(get("/api/vehicles/XYZ789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("CX-5"));
    }

    @Test
    void updateVehicle_shouldReturnUpdated() throws Exception {
        VehicleDTO dto = new VehicleDTO("NEW456", "Kia", "Sportage", 2024);
        Vehicle updated = new Vehicle(1L, "NEW456", "Kia", "Sportage", 2024, null);

        when(vehicleService.update(eq(1L), any())).thenReturn(updated);

        mockMvc.perform(put("/api/vehicles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("Sportage"));
    }
}
