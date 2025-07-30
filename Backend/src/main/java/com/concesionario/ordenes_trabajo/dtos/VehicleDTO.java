package com.concesionario.ordenes_trabajo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {
    private String licensePlate;
    private String brand;
    private String model;
    private Integer year;
}
