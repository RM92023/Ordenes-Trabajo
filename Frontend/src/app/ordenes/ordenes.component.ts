import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../services/api.service';

@Component({
  selector: 'app-ordenes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './ordenes.component.html',
  styleUrls: ['./ordenes.component.scss'],
})
export class OrdenesComponent {
  licensePlate = '';
  ordenes: any[] = [];
  nuevaOrden = {
    tipo: '',
    descripcion: '',
    fecha: '',
    estado: '',
    costo: 0,
  };
  mensaje = '';
  error = '';
  vista = 'gestionar';
  estados = ['PENDING', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED'];

  constructor(private api: ApiService) {}

  buscarOrdenes() {
    if (!this.licensePlate) {
      this.error = 'Debe ingresar una placa.';
      this.mensaje = '';
      this.ordenes = [];
      return;
    }

    this.api.getOrdenesPorVehiculo(this.licensePlate).subscribe({
      next: (res) => {
        this.ordenes = res;
        if (res.length === 0) {
          this.mensaje = 'No se encontró ningún vehículo con esa placa para mantenimiento.';
        } else {
          this.mensaje = '';
        }
        this.error = '';
      },
      error: (err) => {
        this.error = err.error?.message || 'Error al obtener órdenes.';
        this.ordenes = [];
        this.mensaje = '';
      },
    });
  }

  crearOrden() {
    if (
      !this.licensePlate ||
      !this.nuevaOrden.tipo ||
      !this.nuevaOrden.descripcion ||
      !this.nuevaOrden.fecha ||
      !this.nuevaOrden.estado ||
      !this.nuevaOrden.costo
    ) {
      this.error = 'Completa todos los campos.';
      return;
    }

    const data = {
      licensePlate: this.licensePlate,
      type: this.nuevaOrden.tipo,
      description: this.nuevaOrden.descripcion,
      date: this.nuevaOrden.fecha,
      status: this.nuevaOrden.estado,
      cost: this.nuevaOrden.costo,
    };

    this.api.createOrden(data).subscribe({
      next: () => {
        this.mensaje = 'Orden creada con éxito';
        this.error = '';
        this.nuevaOrden = {
          tipo: '',
          descripcion: '',
          fecha: '',
          estado: '',
          costo: 0,
        };
        this.buscarOrdenes();
      },
      error: (err) => {
        this.error = err.error?.message || 'Error al crear orden';
        this.mensaje = '';
      },
    });
  }

  actualizarEstado(id: number, nuevoEstado: string) {
    this.api.updateStatus(id, nuevoEstado).subscribe({
      next: () => {
        this.mensaje = 'Estado actualizado con éxito';
        this.error = '';
        this.buscarOrdenes();
      },
      error: (err) => {
        this.error = err.error?.message || 'Error al actualizar el estado';
        this.mensaje = '';
      },
    });
  }

  onEstadoChange(orden: any) {
    this.actualizarEstado(orden.id, orden.status);
  }
}
