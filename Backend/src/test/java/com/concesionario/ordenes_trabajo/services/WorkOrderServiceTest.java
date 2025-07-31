package com.concesionario.ordenes_trabajo.services;

import com.concesionario.ordenes_trabajo.dtos.WorkOrderDTO;
import com.concesionario.ordenes_trabajo.enums.WorkOrderStatus;
import com.concesionario.ordenes_trabajo.exceptions.BusinessRuleException;
import com.concesionario.ordenes_trabajo.exceptions.ResourceNotFoundException;
import com.concesionario.ordenes_trabajo.models.Vehicle;
import com.concesionario.ordenes_trabajo.models.WorkOrder;
import com.concesionario.ordenes_trabajo.repositories.IVehicleRepository;
import com.concesionario.ordenes_trabajo.repositories.IWorkOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkOrderServiceTest {

    @Mock
    private IWorkOrderRepository workOrderRepository;

    @Mock
    private IVehicleRepository vehicleRepository;

    @InjectMocks
    private WorkOrderService workOrderService;

    private Vehicle mockVehicle;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockVehicle = new Vehicle(1L, "ABC123", "Mazda", "CX5", 2020, null);
    }

    @Test
    void createWorkOrder_successfully() {
        WorkOrderDTO dto = new WorkOrderDTO("repair", "Cambio de aceite", LocalDate.now(), 150.0, WorkOrderStatus.PENDING, "ABC123");

        when(vehicleRepository.findByLicensePlate("ABC123")).thenReturn(Optional.of(mockVehicle));
        when(workOrderRepository.existsByVehicleIdAndStatusIn(eq(1L), anyList())).thenReturn(false);
        when(workOrderRepository.save(any(WorkOrder.class))).thenAnswer(i -> i.getArgument(0));

        WorkOrder result = workOrderService.createWorkOrder(dto);

        assertNotNull(result);
        assertEquals("repair", result.getType());
        assertEquals("ABC123", result.getVehicle().getLicensePlate());
    }

    @Test
    void createWorkOrder_fails_ifVehicleHasActiveOrder() {
        WorkOrderDTO dto = new WorkOrderDTO("repair", "Motor", LocalDate.now(), 100.0, WorkOrderStatus.PENDING, "ABC123");

        when(vehicleRepository.findByLicensePlate("ABC123")).thenReturn(Optional.of(mockVehicle));
        when(workOrderRepository.existsByVehicleIdAndStatusIn(eq(1L), anyList())).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> workOrderService.createWorkOrder(dto));
    }

    @Test
    void updateStatus_successfully() {
        WorkOrder order = new WorkOrder(1L, "repair", "Motor", LocalDate.now(), 200.0, WorkOrderStatus.PENDING, mockVehicle);

        when(workOrderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(workOrderRepository.save(any(WorkOrder.class))).thenAnswer(i -> i.getArgument(0));

        WorkOrder result = workOrderService.updateStatus(1L, WorkOrderStatus.IN_PROGRESS);

        assertEquals(WorkOrderStatus.IN_PROGRESS, result.getStatus());
    }

    @Test
    void updateStatus_fails_ifCompleted() {
        WorkOrder order = new WorkOrder(1L, "repair", "Motor", LocalDate.now(), 200.0, WorkOrderStatus.COMPLETED, mockVehicle);

        when(workOrderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(BusinessRuleException.class, () -> workOrderService.updateStatus(1L, WorkOrderStatus.IN_PROGRESS));
    }

    @Test
    void getByLicensePlate_successfully() {
        when(vehicleRepository.findByLicensePlate("ABC123")).thenReturn(Optional.of(mockVehicle));
        when(workOrderRepository.findByVehicle_LicensePlate("ABC123")).thenReturn(List.of(
                new WorkOrder(1L, "repair", "Frenos", LocalDate.now(), 200.0, WorkOrderStatus.PENDING, mockVehicle)
        ));

        var result = workOrderService.getByLicensePlate("ABC123");
        assertEquals(1, result.size());
    }

    @Test
    void getById_shouldThrow_ifNotFound() {
        when(workOrderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> workOrderService.getById(999L));
    }

    @Test
    void getAll_shouldReturnAllWorkOrders() {
        List<WorkOrder> mockOrders = List.of(
                new WorkOrder(1L, "repair", "Cambio aceite", LocalDate.now(), 120.0, WorkOrderStatus.PENDING, mockVehicle),
                new WorkOrder(2L, "maintenance", "Alineación", LocalDate.now(), 90.0, WorkOrderStatus.IN_PROGRESS, mockVehicle)
        );

        when(workOrderRepository.findAll()).thenReturn(mockOrders);

        List<WorkOrder> result = workOrderService.getAll();

        assertEquals(2, result.size());
        assertEquals("repair", result.get(0).getType());
        assertEquals("maintenance", result.get(1).getType());
    }

    @Test
    void getDTOById_shouldReturnDTO() {
        WorkOrder order = new WorkOrder(1L, "repair", "Motor", LocalDate.now(), 100.0, WorkOrderStatus.PENDING, mockVehicle);
        when(workOrderRepository.findById(1L)).thenReturn(Optional.of(order));

        var dto = workOrderService.getDTOById(1L);

        assertEquals("repair", dto.getType());
        assertEquals("Motor", dto.getDescription());
        assertEquals("ABC123", dto.getLicensePlate());
    }

    @Test
    void getAllDTO_shouldReturnListOfDTOs() {
        List<WorkOrder> mockOrders = List.of(
                new WorkOrder(1L, "repair", "Cambio aceite", LocalDate.now(), 120.0, WorkOrderStatus.PENDING, mockVehicle),
                new WorkOrder(2L, "maintenance", "Alineación", LocalDate.now(), 90.0, WorkOrderStatus.IN_PROGRESS, mockVehicle)
        );

        when(workOrderRepository.findAll()).thenReturn(mockOrders);

        var result = workOrderService.getAllDTO();

        assertEquals(2, result.size());
        assertEquals("repair", result.get(0).getType());
        assertEquals("maintenance", result.get(1).getType());
    }

    @Test
    void getDTOsByLicensePlate_shouldReturnDTOs() {
        List<WorkOrder> mockOrders = List.of(
                new WorkOrder(1L, "repair", "Motor", LocalDate.now(), 100.0, WorkOrderStatus.PENDING, mockVehicle)
        );

        when(vehicleRepository.findByLicensePlate("ABC123")).thenReturn(Optional.of(mockVehicle));
        when(workOrderRepository.findByVehicle_LicensePlate("ABC123")).thenReturn(mockOrders);

        var dtos = workOrderService.getDTOsByLicensePlate("ABC123");

        assertEquals(1, dtos.size());
        assertEquals("repair", dtos.get(0).getType());
        assertEquals("Motor", dtos.get(0).getDescription());
    }
}
