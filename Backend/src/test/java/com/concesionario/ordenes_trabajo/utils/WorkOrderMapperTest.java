package com.concesionario.ordenes_trabajo.utils;

import com.concesionario.ordenes_trabajo.dtos.WorkOrderResponseDTO;
import com.concesionario.ordenes_trabajo.enums.WorkOrderStatus;
import com.concesionario.ordenes_trabajo.models.Vehicle;
import com.concesionario.ordenes_trabajo.models.WorkOrder;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class WorkOrderMapperTest {

    @Test
    void toDTO_shouldMapCorrectly() {
        // Arrange
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setLicensePlate("ABC123");

        WorkOrder order = new WorkOrder();
        order.setId(10L);
        order.setType("Revisión");
        order.setDescription("Cambio de aceite");
        order.setDate(LocalDate.of(2024, 5, 10));
        order.setCost(150000.0);
        order.setStatus(WorkOrderStatus.PENDING);
        order.setVehicle(vehicle);

        // Act
        WorkOrderResponseDTO dto = WorkOrderMapper.toDTO(order);

        // Assert
        assertEquals(10L, dto.getId());
        assertEquals("Revisión", dto.getType());
        assertEquals("Cambio de aceite", dto.getDescription());
        assertEquals(LocalDate.of(2024, 5, 10), dto.getDate());
        assertEquals(150000.0, dto.getCost());
        assertEquals(WorkOrderStatus.PENDING, dto.getStatus());
        assertEquals("ABC123", dto.getLicensePlate());
    }
}
