package com.concesionario.ordenes_trabajo.services;

import com.concesionario.ordenes_trabajo.dtos.WorkOrderDTO;
import com.concesionario.ordenes_trabajo.models.WorkOrder;

import java.util.List;

public interface IWorkOrderService {
    WorkOrder createWorkOrder(WorkOrderDTO dto);
    List<WorkOrder> getByVehicleId(Long vehicleId);
}
