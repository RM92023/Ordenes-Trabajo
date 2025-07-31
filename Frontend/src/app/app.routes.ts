import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { VehiculosComponent } from './vehiculos/vehiculos.component';
import { OrdenesComponent } from './ordenes/ordenes.component';
import { authGuard } from '../guards/auth.guard'; 

export const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'vehiculos', component: VehiculosComponent, canActivate: [authGuard] },
  { path: 'ordenes', component: OrdenesComponent, canActivate: [authGuard] },
];
