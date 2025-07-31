# Sistema de Órdenes de Trabajo - Concesionario

Este proyecto es una prueba técnica **fullstack** que gestiona órdenes de trabajo de vehículos.  
Incluye:

- Backend: Java + Spring Boot + PostgreSQL + JWT
- Frontend: Angular + Nginx
- Docker y Docker Compose

## Estructura del proyecto

```
/
├── Backend/         # API REST en Spring Boot
├── Frontend/        # App Angular
├── docker-compose.yml
└── README.md
```

## Cómo levantar todo con Docker

### 1. Construir y levantar servicios

Desde la raíz del proyecto:

```bash
docker compose up --build -d
```

Esto levantará:
- PostgreSQL (`localhost:5432`)
- API Backend (`localhost:8080`)
- App Angular (`localhost:4200`)

### 2. Verificar estado

```bash
docker ps
```

## Accesos

| Servicio     | URL                              |
|--------------|----------------------------------|
| Frontend     | http://localhost:4200            |
| Swagger API  | http://localhost:8080/swagger-ui.html |
| PostgreSQL   | Host interno `db`, puerto `5432` |

## Base de datos

- Usuario: `admin`
- Contraseña: `admin`
- Nombre: `ordenes_trabajo`

## Pruebas unitarias

El backend contiene pruebas que pueden ejecutarse con:

```bash
cd Backend
./gradlew test
```

---

## Desarrollo sin Docker

### Backend

```bash
cd Backend
./gradlew bootRun
```

### Frontend

```bash
cd Frontend
npm install
ng serve
```

## Autor

<div style="display: flex; align-items: center; gap: 10px; margin-top: 1rem;">
  <img src="https://avatars.githubusercontent.com/u/114312031?v=4" alt="Robinson Muñetón" width="60" height="60" style="border-radius: 50%;">
  <div>
    <strong>Robinson Muñetón Jaramillo</strong><br>
    <a href="https://github.com/rm92023" target="_blank">github.com/rm92023</a>
  </div>
</div>