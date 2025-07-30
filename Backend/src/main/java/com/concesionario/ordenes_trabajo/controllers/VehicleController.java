package com.concesionario.ordenes_trabajo.controllers;

import com.concesionario.ordenes_trabajo.dtos.VehicleDTO;
import com.concesionario.ordenes_trabajo.models.Vehicle;
import com.concesionario.ordenes_trabajo.services.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@RequestBody VehicleDTO dto) {
        return ResponseEntity.ok(vehicleService.createVehicle(dto));
    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> getAll() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }
}
