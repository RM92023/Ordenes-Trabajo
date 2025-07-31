package com.concesionario.ordenes_trabajo.dtos;

import com.concesionario.ordenes_trabajo.enums.WorkOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusDTO {
    private WorkOrderStatus status;
}
