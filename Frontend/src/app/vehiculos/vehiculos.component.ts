import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService, VehiculoDTO } from '../services/api.service';

@Component({
  selector: 'app-vehiculos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './vehiculos.component.html',
  styleUrls: ['./vehiculos.component.scss']
})
export class VehiculosComponent implements OnInit {
  vehiculos: VehiculoDTO[] = [];
  licensePlate = '';
  brand = '';
  model = '';
  year: number | null = null;

  error = '';
  success = '';

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.getVehiculos();
  }

  getVehiculos() {
    this.api.getVehiculos().subscribe({
      next: (data) => this.vehiculos = data,
      error: () => this.error = 'No se pudo cargar la lista de vehículos.'
    });
  }

  crearVehiculo() {
    this.error = '';
    this.success = '';

    if (!this.licensePlate || !this.brand || !this.model || !this.year) {
      this.error = 'Todos los campos son obligatorios.';
      return;
    }

    const nuevoVehiculo: VehiculoDTO = {
      licensePlate: this.licensePlate,
      brand: this.brand,
      model: this.model,
      year: this.year
    };

    this.api.createVehiculo(nuevoVehiculo).subscribe({
      next: () => {
        this.success = 'Vehículo creado correctamente.';
        this.licensePlate = '';
        this.brand = '';
        this.model = '';
        this.year = null;
        this.getVehiculos();
      },
      error: (err) => {
        this.error = err.error?.message || 'Error al crear el vehículo.';
      }
    });
  }
}
