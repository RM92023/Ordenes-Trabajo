package com.concesionario.ordenes_trabajo.repositories;

import com.concesionario.ordenes_trabajo.enums.WorkOrderStatus;
import com.concesionario.ordenes_trabajo.models.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IWorkOrderRepository extends JpaRepository<WorkOrder, Long> {
    List<WorkOrder> findByVehicleId(Long vehicleId);
    boolean existsByVehicleIdAndStatusIn(Long vehicleId, List<WorkOrderStatus> statuses);
}
