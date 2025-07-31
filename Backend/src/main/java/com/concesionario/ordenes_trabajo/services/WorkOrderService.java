package com.concesionario.ordenes_trabajo.services;

import com.concesionario.ordenes_trabajo.dtos.WorkOrderDTO;
import com.concesionario.ordenes_trabajo.dtos.WorkOrderResponseDTO;
import com.concesionario.ordenes_trabajo.enums.WorkOrderStatus;
import com.concesionario.ordenes_trabajo.exceptions.BusinessRuleException;
import com.concesionario.ordenes_trabajo.exceptions.ResourceNotFoundException;
import com.concesionario.ordenes_trabajo.models.Vehicle;
import com.concesionario.ordenes_trabajo.models.WorkOrder;
import com.concesionario.ordenes_trabajo.repositories.IVehicleRepository;
import com.concesionario.ordenes_trabajo.repositories.IWorkOrderRepository;
import com.concesionario.ordenes_trabajo.utils.WorkOrderMapper;
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
        if (dto.getLicensePlate() == null || dto.getLicensePlate().isBlank()) {
            throw new BusinessRuleException("License plate is required.");
        }

        Vehicle vehicle = vehicleRepository.findByLicensePlate(dto.getLicensePlate())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle with license plate " + dto.getLicensePlate() + " not found."));

        List<WorkOrderStatus> activeStatuses = Arrays.asList(WorkOrderStatus.PENDING, WorkOrderStatus.IN_PROGRESS);
        boolean hasActiveOrder = workOrderRepository.existsByVehicleIdAndStatusIn(vehicle.getId(), activeStatuses);

        if (hasActiveOrder) {
            throw new BusinessRuleException("Vehicle with license plate " + dto.getLicensePlate() + " already has an active work order.");
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
    public WorkOrder updateStatus(Long orderID, WorkOrderStatus newStatus) {
        WorkOrder order = workOrderRepository.findById(orderID)
                .orElseThrow(() -> new ResourceNotFoundException("Work order with ID " + orderID + " not found."));

        if (newStatus == null) {
            throw new BusinessRuleException("New status cannot be null.");
        }

        if (order.getStatus() == WorkOrderStatus.COMPLETED || order.getStatus() == WorkOrderStatus.CANCELLED) {
            throw new BusinessRuleException("Cannot update status of a completed or cancelled work order.");
        }

        order.setStatus(newStatus);
        return workOrderRepository.save(order);
    }

    @Override
    public WorkOrder getById(Long id) {
        return workOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work order with ID " + id + " not found."));
    }

    @Override
    public List<WorkOrder> getByLicensePlate(String licensePlate) {
        Vehicle vehicle = vehicleRepository.findByLicensePlate(licensePlate)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle with license plate " + licensePlate + " not found."));

        return workOrderRepository.findByVehicle_LicensePlate(vehicle.getLicensePlate());
    }

    @Override
    public List<WorkOrder> getAll() {
        return workOrderRepository.findAll();
    }

    @Override
    public WorkOrderResponseDTO getDTOById(Long id) {
        return WorkOrderMapper.toDTO(getById(id));
    }

    @Override
    public List<WorkOrderResponseDTO> getAllDTO() {
        return workOrderRepository.findAll().stream()
                .map(WorkOrderMapper::toDTO)
                .toList();
    }

    @Override
    public List<WorkOrderResponseDTO> getDTOsByLicensePlate(String licensePlate) {
        return getByLicensePlate(licensePlate).stream()
                .map(WorkOrderMapper::toDTO)
                .toList();
    }
}
