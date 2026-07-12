# 🛒 MiniMarket Plus API

## Descripción

MiniMarket Plus API es una aplicación backend desarrollada en **Spring Boot 3**, cuyo objetivo es gestionar las operaciones de un minimarket mediante una API REST segura.

El proyecto implementa autenticación y autorización con Spring Security y JWT, documentación de la API mediante OpenAPI (Swagger) e hipermedia utilizando Spring HATEOAS, siguiendo las buenas prácticas vistas durante la asignatura Desarrollo Backend II.

---

# Objetivos

- Desarrollar una API REST utilizando Spring Boot.
- Gestionar productos, inventario, usuarios y carrito de compras.
- Implementar autenticación mediante JWT.
- Aplicar control de acceso basado en roles.
- Documentar los servicios utilizando OpenAPI.
- Implementar navegación mediante HATEOAS.
- Realizar pruebas unitarias e integración.

---

# Funcionalidades

El sistema permite:

- Gestión de productos.
- Gestión de inventario.
- Gestión de usuarios.
- Gestión del carrito de compras.
- Gestión de ventas.
- Autenticación mediante JWT.
- Protección de endpoints con Spring Security.
- Documentación interactiva mediante Swagger UI.
- Navegación entre recursos mediante HATEOAS.

---

# Tecnologías utilizadas

| Tecnología | Descripción |
|------------|-------------|
| Java 17 | Lenguaje de programación |
| Spring Boot 3 | Framework principal |
| Spring Security | Seguridad |
| Spring Data JPA | Persistencia |
| Spring HATEOAS | Hipermedia |
| SpringDoc OpenAPI | Documentación |
| Swagger UI | Visualización de la API |
| JWT | Autenticación |
| H2 Database | Base de datos |
| Maven | Gestión de dependencias |
| JUnit 5 | Pruebas |
| Mockito | Mocking |
| JaCoCo | Cobertura |

---

# Arquitectura

El proyecto sigue una arquitectura en capas.

```
Cliente
   │
   ▼
Controllers
   │
   ▼
Services
   │
   ▼
Repositories
   │
   ▼
Base de datos
```

---

# Estructura del proyecto

```
src
├── main
│   ├── java
│   │   └── com.minimarket
│   │       ├── config
│   │       ├── controller
│   │       ├── entity
│   │       ├── hateoas
│   │       ├── repository
│   │       ├── security
│   │       └── service
│   └── resources
└── test
```

---

# Documentación de la API

La documentación se genera automáticamente utilizando **SpringDoc OpenAPI**.

Una vez iniciada la aplicación puede accederse a:

### Swagger UI

```
http://localhost:8081/swagger-ui/index.html
```

### OpenAPI JSON

```
http://localhost:8081/v3/api-docs
```

La documentación incluye:

- Endpoints REST
- Parámetros
- Códigos de respuesta
- Esquemas de datos
- Seguridad mediante JWT

---

# Implementación de HATEOAS

Los recursos principales incorporan enlaces dinámicos utilizando **Spring HATEOAS**.

Se implementaron assemblers para las entidades:

- Producto
- Carrito
- Inventario
- Usuario

Las respuestas incluyen enlaces como:

- self
- productos
- inventario
- carrito
- usuarios

permitiendo mejorar la navegabilidad de la API.

---

# Seguridad

La aplicación incorpora:

- Spring Security
- JWT Authentication
- Control de acceso basado en roles (RBAC)
- OAuth2 Resource Server
- LDAP embebido

---

# Ejecución del proyecto

## Requisitos

- Java 17 o superior

## Ejecutar

Windows

```bash
.\mvnw.cmd spring-boot:run
```

Linux

```bash
./mvnw spring-boot:run
```

La aplicación quedará disponible en:

```
http://localhost:8081
```

---

# Pruebas

Ejecutar todas las pruebas:

```bash
.\mvnw test
```

Generar reporte JaCoCo:

```bash
.\mvnw verify
```

Reporte:

```
target/site/jacoco/index.html
```

---

# Integrantes

Proyecto desarrollado para la asignatura **Desarrollo Backend II (PBY2202)**.

- Sebastián Tapia
- Sofía Medina
- Ángel Cáceres
