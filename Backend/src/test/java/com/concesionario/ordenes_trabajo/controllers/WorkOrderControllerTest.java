package com.concesionario.ordenes_trabajo.controllers;

import com.concesionario.ordenes_trabajo.dtos.UpdateStatusDTO;
import com.concesionario.ordenes_trabajo.dtos.WorkOrderDTO;
import com.concesionario.ordenes_trabajo.dtos.WorkOrderResponseDTO;
import com.concesionario.ordenes_trabajo.enums.WorkOrderStatus;
import com.concesionario.ordenes_trabajo.models.Vehicle;
import com.concesionario.ordenes_trabajo.models.WorkOrder;
import com.concesionario.ordenes_trabajo.services.WorkOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class WorkOrderControllerTest {

    private MockMvc mockMvc;
    private WorkOrderService workOrderService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        workOrderService = mock(WorkOrderService.class);
        WorkOrderController controller = new WorkOrderController(workOrderService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Para soportar LocalDate
    }

    @Test
    void createWorkOrder_shouldReturnCreated() throws Exception {
        WorkOrderDTO dto = new WorkOrderDTO();
        dto.setType("Mantenimiento");
        dto.setDescription("Cambio de aceite");
        dto.setDate(LocalDate.of(2024, 1, 1));
        dto.setCost(150000.0);
        dto.setStatus(WorkOrderStatus.PENDING);
        dto.setLicensePlate("ABC123");

        WorkOrder order = new WorkOrder();
        order.setId(1L);
        order.setType(dto.getType());
        order.setDescription(dto.getDescription());
        order.setDate(dto.getDate());
        order.setCost(dto.getCost());
        order.setStatus(dto.getStatus());

        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setLicensePlate(dto.getLicensePlate());
        order.setVehicle(vehicle);

        when(workOrderService.createWorkOrder(dto)).thenReturn(order);

        mockMvc.perform(post("/api/work-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.licensePlate").value("ABC123"))
                .andExpect(jsonPath("$.description").value("Cambio de aceite"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void getAllWorkOrders_shouldReturnList() throws Exception {
        WorkOrderResponseDTO dto = new WorkOrderResponseDTO(
                1L, "repair", "Motor", LocalDate.now(), 200.0, WorkOrderStatus.COMPLETED, "ABC123"
        );

        when(workOrderService.getAllDTO()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/work-orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("repair"));
    }

    @Test
    void getWorkOrderById_shouldReturnDTO() throws Exception {
        WorkOrderResponseDTO dto = new WorkOrderResponseDTO(
                1L, "repair", "Frenos", LocalDate.now(), 150.0, WorkOrderStatus.IN_PROGRESS, "XYZ789"
        );

        when(workOrderService.getDTOById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/work-orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Frenos"));
    }

    @Test
    void getOrdersByLicensePlate_shouldReturnList() throws Exception {
        WorkOrderResponseDTO dto = new WorkOrderResponseDTO(
                2L, "repair", "Revisión general", LocalDate.now(), 100.0, WorkOrderStatus.PENDING, "ABC123"
        );

        when(workOrderService.getDTOsByLicensePlate("ABC123")).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/work-orders/vehicle/ABC123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].licensePlate").value("ABC123"));
    }

    @Test
    void updateStatus_shouldReturnUpdatedDTO() throws Exception {
        UpdateStatusDTO update = new UpdateStatusDTO(WorkOrderStatus.COMPLETED);

        WorkOrder updated = new WorkOrder();
        updated.setId(5L);
        updated.setType("maintenance");
        updated.setDescription("Cambio de frenos");
        updated.setDate(LocalDate.now());
        updated.setCost(400.0);
        updated.setStatus(WorkOrderStatus.COMPLETED);

        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("TEST456");
        updated.setVehicle(vehicle);

        when(workOrderService.updateStatus(eq(5L), any())).thenReturn(updated);

        mockMvc.perform(put("/api/work-orders/5/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.licensePlate").value("TEST456"));
    }
}
