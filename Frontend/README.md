# Frontend - Órdenes de Trabajo

Este es el frontend del sistema, desarrollado con **Angular** y servido mediante **Nginx**.

## Tecnologías

- Angular
- TypeScript
- Nginx (para producción)
- Docker

## Ejecutar en desarrollo

```bash
npm install
ng serve
```

Acceder en:  
[http://localhost:4200](http://localhost:4200)

## Cambiar URL del Backend

En `src/app/services/services.api.ts`:

```ts
export const environment = {
  private readonly baseUrl = 'http://localhost:8080';
  private readonly apiUrl = `${this.baseUrl}/api`;

};
```

## Docker

Ya configurado con Dockerfile.

```bash
docker build -t ordenes-trabajo-frontend .
docker run -p 4200:80 ordenes-trabajo-frontend
```

---

## Output Angular

El build se genera en:

```
dist/frontend/
```

Y se copia automáticamente en `/usr/share/nginx/html` durante el build.

## Autor

<div style="display: flex; align-items: center; gap: 10px; margin-top: 1rem;">
  <img src="https://avatars.githubusercontent.com/u/114312031?v=4" alt="Robinson Muñetón" width="60" height="60" style="border-radius: 50%;">
  <div>
    <strong>Robinson Muñetón Jaramillo</strong><br>
    <a href="https://github.com/rm92023" target="_blank">github.com/rm92023</a>
  </div>
</div>