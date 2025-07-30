package com.concesionario.ordenes_trabajo.services;

import com.concesionario.ordenes_trabajo.dtos.VehicleDTO;
import com.concesionario.ordenes_trabajo.models.Vehicle;

import java.util.List;

public interface IVehicleService {
    Vehicle createVehicle(VehicleDTO dto);
    List<Vehicle> getAllVehicles();
    Vehicle getVehicleById(Long id);
}
