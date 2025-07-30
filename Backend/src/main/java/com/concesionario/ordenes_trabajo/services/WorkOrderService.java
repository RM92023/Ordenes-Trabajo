package com.concesionario.ordenes_trabajo.services;

import com.concesionario.ordenes_trabajo.dtos.WorkOrderDTO;
import com.concesionario.ordenes_trabajo.enums.WorkOrderStatus;
import com.concesionario.ordenes_trabajo.exceptions.BusinessRuleException;
import com.concesionario.ordenes_trabajo.exceptions.ResourceNotFoundException;
import com.concesionario.ordenes_trabajo.models.Vehicle;
import com.concesionario.ordenes_trabajo.models.WorkOrder;
import com.concesionario.ordenes_trabajo.repositories.IVehicleRepository;
import com.concesionario.ordenes_trabajo.repositories.IWorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkOrderService implements IWorkOrderService {

    private final IWorkOrderRepository workOrderRepository;
    private final IVehicleRepository vehicleRepository;

    @Override
    public WorkOrder createWorkOrder(WorkOrderDTO dto) {
        if (dto.getType() == null || dto.getType().isBlank()) {
            throw new BusinessRuleException("Order type is required.");
        }
        if (dto.getDescription() == null || dto.getDescription().isBlank()) {
            throw new BusinessRuleException("Order description is required.");
        }
        if (dto.getDate() == null || dto.getDate().isAfter(LocalDate.now())) {
            throw new BusinessRuleException("Invalid order date.");
        }
        if (dto.getCost() == null || dto.getCost() < 0) {
            throw new BusinessRuleException("Order cost must be greater than or equal to 0.");
        }
        if (dto.getVehicleId() == null) {
            throw new BusinessRuleException("Vehicle ID is required.");
        }

        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle with id " + dto.getVehicleId() + " not found."));

        List<WorkOrderStatus> activeStatuses = Arrays.asList(WorkOrderStatus.PENDING, WorkOrderStatus.IN_PROGRESS);
        boolean hasActiveOrder = workOrderRepository.existsByVehicleIdAndStatusIn(vehicle.getId(), activeStatuses);

        if (hasActiveOrder) {
            throw new BusinessRuleException("Vehicle with id " + dto.getVehicleId() + " already has an active work order.");
        }

        WorkOrder order = new WorkOrder();
        order.setType(dto.getType());
        order.setDescription(dto.getDescription());
        order.setDate(dto.getDate());
        order.setCost(dto.getCost());
        order.setStatus(dto.getStatus());
        order.setVehicle(vehicle);

        return workOrderRepository.save(order);
    }

    @Override
    public List<WorkOrder> getByVehicleId(Long vehicleId) {
        if (!vehicleRepository.existsById(vehicleId)) {
            throw new ResourceNotFoundException("Vehicle with id " + vehicleId + " not found.");
        }

        return workOrderRepository.findByVehicleId(vehicleId);
    }
}
