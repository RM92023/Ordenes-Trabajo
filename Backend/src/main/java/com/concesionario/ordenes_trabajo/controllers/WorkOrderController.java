package com.concesionario.ordenes_trabajo.controllers;

import com.concesionario.ordenes_trabajo.dtos.UpdateStatusDTO;
import com.concesionario.ordenes_trabajo.dtos.WorkOrderDTO;
import com.concesionario.ordenes_trabajo.dtos.WorkOrderResponseDTO;
import com.concesionario.ordenes_trabajo.models.WorkOrder;
import com.concesionario.ordenes_trabajo.services.WorkOrderService;
import com.concesionario.ordenes_trabajo.utils.WorkOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/work-orders")
@RequiredArgsConstructor
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    @PostMapping
    public ResponseEntity<WorkOrderResponseDTO> create(@RequestBody WorkOrderDTO dto) {
        WorkOrder order = workOrderService.createWorkOrder(dto);
        return new ResponseEntity<>(WorkOrderMapper.toDTO(order), HttpStatus.CREATED);
    }

    @GetMapping("/vehicle/{licensePlate}")
    public ResponseEntity<List<WorkOrderResponseDTO>> getOrdersByLicensePlate(@PathVariable String licensePlate) {
        return ResponseEntity.ok(workOrderService.getDTOsByLicensePlate(licensePlate));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<WorkOrderResponseDTO> updateStatus(@PathVariable Long id, @RequestBody UpdateStatusDTO dto) {
        WorkOrder updated = workOrderService.updateStatus(id, dto.getStatus());
        return ResponseEntity.ok(WorkOrderMapper.toDTO(updated));
    }

    @GetMapping
    public ResponseEntity<List<WorkOrderResponseDTO>> getAllWorkOrders() {
        return ResponseEntity.ok(workOrderService.getAllDTO());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkOrderResponseDTO> getWorkOrderById(@PathVariable Long id){
        return ResponseEntity.ok(workOrderService.getDTOById(id));
    }
}
