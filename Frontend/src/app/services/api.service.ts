import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

interface LoginRequest {
  username: string;
  password: string;
}

export interface VehiculoDTO {
  licensePlate: string;
  brand: string;
  model: string;
  year: number;
}

export interface OrdenTrabajoDTO {
  licensePlate: string;
  type: string;
  description: string;
  date: string;
  status: string;
  cost: number;
}


@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private readonly baseUrl = 'http://localhost:8080';
  private readonly apiUrl = `${this.baseUrl}/api`;

  constructor(private http: HttpClient) {}

  login(data: LoginRequest): Observable<string> {
    return this.http.post(`${this.baseUrl}/auth/login`, data, { responseType: 'text' });
  }

  getVehiculos(): Observable<VehiculoDTO[]> {
    return this.http.get<VehiculoDTO[]>(`${this.apiUrl}/vehicles`);
  }

  createVehiculo(data: VehiculoDTO): Observable<VehiculoDTO> {
    return this.http.post<VehiculoDTO>(`${this.apiUrl}/vehicles`, data);
  }

  getVehiculoByPlaca(placa: string): Observable<VehiculoDTO> {
    return this.http.get<VehiculoDTO>(`${this.apiUrl}/vehicles/${placa}`);
  }

  updateVehiculo(id: number, data: VehiculoDTO): Observable<VehiculoDTO> {
    return this.http.put<VehiculoDTO>(`${this.apiUrl}/vehicles/${id}`, data);
  }

  getOrdenes(): Observable<OrdenTrabajoDTO[]> {
    return this.http.get<OrdenTrabajoDTO[]>(`${this.apiUrl}/work-orders`);
  }

  getOrdenesPorVehiculo(placa: string): Observable<OrdenTrabajoDTO[]> {
    return this.http.get<OrdenTrabajoDTO[]>(`${this.apiUrl}/work-orders/vehicle/${placa}`);
  }

  getOrdenById(id: number): Observable<OrdenTrabajoDTO> {
    return this.http.get<OrdenTrabajoDTO>(`${this.apiUrl}/work-orders/${id}`);
  }

  createOrden(data: OrdenTrabajoDTO): Observable<OrdenTrabajoDTO> {
    return this.http.post<OrdenTrabajoDTO>(`${this.apiUrl}/work-orders`, data);
  }

  updateStatus(id: number, status: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/work-orders/${id}/status`, { status });
  }
}
