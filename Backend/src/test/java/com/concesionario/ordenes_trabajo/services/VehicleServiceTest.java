package com.concesionario.ordenes_trabajo.services;

import com.concesionario.ordenes_trabajo.dtos.VehicleDTO;
import com.concesionario.ordenes_trabajo.exceptions.DuplicatedResourceException;
import com.concesionario.ordenes_trabajo.exceptions.ResourceNotFoundException;
import com.concesionario.ordenes_trabajo.models.Vehicle;
import com.concesionario.ordenes_trabajo.repositories.IVehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VehicleServiceTest {

    @Mock
    private IVehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleService vehicleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createVehicle_shouldSucceed() {
        VehicleDTO dto = new VehicleDTO("XYZ123", "Toyota", "Corolla", 2023);
        when(vehicleRepository.existsByLicensePlate("XYZ123")).thenReturn(false);

        Vehicle mockVehicle = new Vehicle(1L, "XYZ123", "Toyota", "Corolla", 2023, null);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(mockVehicle);

        Vehicle result = vehicleService.createVehicle(dto);

        assertNotNull(result);
        assertEquals("XYZ123", result.getLicensePlate());
    }

    @Test
    void createVehicle_shouldFailIfPlateExists() {
        VehicleDTO dto = new VehicleDTO("XYZ123", "Toyota", "Corolla", 2023);
        when(vehicleRepository.existsByLicensePlate("XYZ123")).thenReturn(true);

        assertThrows(DuplicatedResourceException.class, () -> vehicleService.createVehicle(dto));
    }

    @Test
    void getVehicleByLicensePlate_shouldReturnVehicle() {
        Vehicle vehicle = new Vehicle(1L, "XYZ123", "Toyota", "Corolla", 2023, null);
        when(vehicleRepository.findByLicensePlate("XYZ123")).thenReturn(Optional.of(vehicle));

        Vehicle result = vehicleService.getVehicleByLicensePlate("XYZ123");

        assertEquals("XYZ123", result.getLicensePlate());
    }

    @Test
    void getVehicleByLicensePlate_shouldFailIfNotFound() {
        when(vehicleRepository.findByLicensePlate("XYZ123")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> vehicleService.getVehicleByLicensePlate("XYZ123"));
    }

    @Test
    void getAllVehicles_shouldReturnList() {
        when(vehicleRepository.findAll()).thenReturn(List.of(
                new Vehicle(1L, "AAA111", "BrandA", "ModelA", 2020, null),
                new Vehicle(2L, "BBB222", "BrandB", "ModelB", 2021, null)
        ));

        List<Vehicle> result = vehicleService.getAllVehicles();

        assertEquals(2, result.size());
    }

    @Test
    void update_shouldUpdateData() {
        Vehicle existing = new Vehicle(1L, "OLD123", "OldBrand", "OldModel", 2010, null);
        VehicleDTO update = new VehicleDTO("NEW456", "NewBrand", "NewModel", 2022);

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(vehicleRepository.existsByLicensePlate("NEW456")).thenReturn(false);
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(i -> i.getArgument(0));

        Vehicle updated = vehicleService.update(1L, update);

        assertEquals("NEW456", updated.getLicensePlate());
        assertEquals("NewBrand", updated.getBrand());
        assertEquals("NewModel", updated.getModel());
        assertEquals(2022, updated.getYear());
    }
}
