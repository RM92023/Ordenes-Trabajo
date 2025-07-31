# Backend - API de Órdenes de Trabajo

Este módulo contiene el backend desarrollado en **Spring Boot** para la gestión de vehículos y sus órdenes.

## Funcionalidades

- CRUD de Vehículos
- CRUD de Órdenes de Trabajo
- Validación de reglas de negocio:
- Un vehículo no puede tener más de una orden activa
- Seguridad con JWT (InMemory User)
- Documentación Swagger

## Tecnologías

- Java 17
- Spring Boot 3
- PostgreSQL
- Spring Security + JWT
- Swagger / OpenAPI
- Gradle

## Usuario de prueba

```json
{
  "username": "admin",
  "password": "admin123"
}
```

## Ejecutar en local

```bash
./gradlew bootRun
```

Accede a Swagger:  
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Docker

Dockerfile ya incluido. Para construir y correr:

```bash
docker build -t ordenes-trabajo-backend .
docker run -p 8080:8080 ordenes-trabajo-backend
```

> Asegúrate de que la base de datos esté disponible en `db:5432`.

## 🧪 Pruebas

```bash
./gradlew test
```
<div style="display: flex; align-items: center; gap: 10px; margin-top: 1rem;">
  <img src="https://avatars.githubusercontent.com/u/114312031?v=4" alt="Robinson Muñetón" width="60" height="60" style="border-radius: 50%;">
  <div>
    <strong>Robinson Muñetón Jaramillo</strong><br>
    <a href="https://github.com/rm92023" target="_blank">github.com/rm92023</a>
  </div>
</div>