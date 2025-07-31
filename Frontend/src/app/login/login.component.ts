import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '../services/api.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  username: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(private api: ApiService, private router: Router) {}

  ngOnInit(): void {
    const token = localStorage.getItem('token');
    if (token) {
      this.router.navigate(['/vehiculos']);
    }
  }

  login(): void {
    if (!this.username || !this.password) {
      this.errorMessage = 'Debes ingresar usuario y contraseña.';
      return;
    }

    this.api.login({ username: this.username, password: this.password })
      .subscribe({
        next: (res: string) => {
          const token = res.replace('Bearer ', '');
          localStorage.setItem('token', token);
          this.router.navigate(['/vehiculos']);
        },
        error: () => {
          this.errorMessage = 'Credenciales incorrectas. Intenta de nuevo.';
        }
      });
  }
}
