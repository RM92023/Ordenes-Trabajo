package com.concesionario.ordenes_trabajo.controllers;

import com.concesionario.ordenes_trabajo.dtos.WorkOrderDTO;
import com.concesionario.ordenes_trabajo.models.WorkOrder;
import com.concesionario.ordenes_trabajo.services.WorkOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/work-orders")
@RequiredArgsConstructor
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    @PostMapping
    public ResponseEntity<WorkOrder> createWorkOrder(@RequestBody WorkOrderDTO dto) {
        return ResponseEntity.ok(workOrderService.createWorkOrder(dto));
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<WorkOrder>> getOrderByVehicle(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(workOrderService.getByVehicleId(vehicleId));
    }

}
