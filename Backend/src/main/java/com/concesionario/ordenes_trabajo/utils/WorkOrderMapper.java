package com.concesionario.ordenes_trabajo.utils;

import com.concesionario.ordenes_trabajo.dtos.WorkOrderResponseDTO;
import com.concesionario.ordenes_trabajo.models.WorkOrder;

public class WorkOrderMapper {

    public static WorkOrderResponseDTO toDTO(WorkOrder order) {
        return new WorkOrderResponseDTO(
                order.getId(),
                order.getType(),
                order.getDescription(),
                order.getDate(),
                order.getCost(),
                order.getStatus(),
                order.getVehicle().getLicensePlate()
        );
    }
}
