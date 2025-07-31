package com.concesionario.ordenes_trabajo.services;

import com.concesionario.ordenes_trabajo.dtos.WorkOrderDTO;
import com.concesionario.ordenes_trabajo.dtos.WorkOrderResponseDTO;
import com.concesionario.ordenes_trabajo.enums.WorkOrderStatus;
import com.concesionario.ordenes_trabajo.models.WorkOrder;

import java.util.List;

public interface IWorkOrderService {
    WorkOrder createWorkOrder(WorkOrderDTO dto);
    WorkOrder updateStatus(Long orderID, WorkOrderStatus newStatus);
    WorkOrder getById(Long id);
    List<WorkOrder> getByLicensePlate(String licensePlate);
    List<WorkOrder> getAll();

    // Métodos con DTO de salida
    WorkOrderResponseDTO getDTOById(Long id);
    List<WorkOrderResponseDTO> getAllDTO();
    List<WorkOrderResponseDTO> getDTOsByLicensePlate(String licensePlate);
}
