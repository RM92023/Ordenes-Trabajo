package com.concesionario.ordenes_trabajo.dtos;

import com.concesionario.ordenes_trabajo.enums.WorkOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkOrderResponseDTO {
    private Long id;
    private String type;
    private String description;
    private LocalDate date;
    private Double cost;
    private WorkOrderStatus status;
    private String licensePlate;
}
