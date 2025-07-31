package com.concesionario.ordenes_trabajo.services;

import com.concesionario.ordenes_trabajo.dtos.VehicleDTO;
import com.concesionario.ordenes_trabajo.exceptions.BusinessRuleException;
import com.concesionario.ordenes_trabajo.exceptions.DuplicatedResourceException;
import com.concesionario.ordenes_trabajo.exceptions.ResourceNotFoundException;
import com.concesionario.ordenes_trabajo.models.Vehicle;
import com.concesionario.ordenes_trabajo.repositories.IVehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService implements IVehicleService {

    private final IVehicleRepository vehicleRepository;

    @Override
    public Vehicle createVehicle(VehicleDTO dto) {
        if (dto.getLicensePlate() == null || dto.getLicensePlate().isBlank()) {
            throw new BusinessRuleException("License plate is required.");
        }
        if (dto.getBrand() == null || dto.getBrand().isBlank()) {
            throw new BusinessRuleException("Brand is required.");
        }
        if (dto.getModel() == null || dto.getModel().isBlank()) {
            throw new BusinessRuleException("Model is required.");
        }
        if (dto.getYear() == null || dto.getYear() < 1900 || dto.getYear() > LocalDate.now().getYear()) {
            throw new BusinessRuleException("Invalid vehicle year.");
        }

        if (vehicleRepository.existsByLicensePlate(dto.getLicensePlate())) {
            throw new DuplicatedResourceException("Vehicle with license plate " + dto.getLicensePlate() + " already exists.");
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate(dto.getLicensePlate());
        vehicle.setBrand(dto.getBrand());
        vehicle.setModel(dto.getModel());
        vehicle.setYear(dto.getYear());

        return vehicleRepository.save(vehicle);
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    @Override
    public Vehicle getVehicleByLicensePlate(String licensePlate) {
        return vehicleRepository.findByLicensePlate(licensePlate)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle with license plate " + licensePlate + " not found."));
    }

    @Override
    public Vehicle update(Long id, VehicleDTO dto) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle with ID " + id + " not found."));

        if (dto.getLicensePlate() != null && !dto.getLicensePlate().isBlank()) {
            if (vehicleRepository.existsByLicensePlate(dto.getLicensePlate())) {
                throw new DuplicatedResourceException("Vehicle with license plate " + dto.getLicensePlate() + " already exists.");
            }
            vehicle.setLicensePlate(dto.getLicensePlate());
        }
        if (dto.getBrand() != null && !dto.getBrand().isBlank()) {
            vehicle.setBrand(dto.getBrand());
        }
        if (dto.getModel() != null && !dto.getModel().isBlank()) {
            vehicle.setModel(dto.getModel());
        }
        if (dto.getYear() != null && dto.getYear() >= 1900 && dto.getYear() <= LocalDate.now().getYear()) {
            vehicle.setYear(dto.getYear());
        }

        return vehicleRepository.save(vehicle);
    }
}
