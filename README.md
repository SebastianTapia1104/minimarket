# 🛒 MiniMarket Plus API

## Descripción

MiniMarket Plus API es una aplicación backend desarrollada con **Spring Boot 3** que implementa una API REST para la administración de un minimarket.

El sistema permite gestionar productos, categorías, inventario, usuarios, ventas y carrito de compras, incorporando mecanismos de autenticación mediante JWT, autorización basada en roles, documentación automática con OpenAPI (Swagger) y navegación entre recursos utilizando Spring HATEOAS.

El proyecto fue desarrollado para la asignatura **Desarrollo Backend II (PBY2202)**.

---

# Funcionalidades

El sistema permite:

- Gestión de productos.
- Gestión de categorías.
- Gestión de inventario.
- Gestión del carrito de compras.
- Gestión de ventas.
- Gestión de usuarios.
- Autenticación mediante JWT.
- Control de acceso por roles.
- Documentación automática con OpenAPI.
- Navegación mediante enlaces HATEOAS.
- Pruebas unitarias e integración.

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
| Embedded LDAP | Autenticación LDAP |
| OAuth2 Resource Server | Seguridad OAuth2 |
| H2 Database | Base de datos en memoria |
| Maven | Gestión de dependencias |
| JUnit 5 | Pruebas unitarias |
| Mockito | Mocking |
| MockMvc | Pruebas de controladores |
| JaCoCo | Cobertura de código |

---

# Arquitectura

El proyecto utiliza una arquitectura en capas.

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
Base de Datos
```

Esta organización permite mantener una separación clara entre la lógica de presentación, negocio y persistencia.

---

# Estructura del proyecto

```
minimarket/
│
├── pom.xml
├── mvnw
├── mvnw.cmd
├── README.md
│
├── docs/
│   └── evidencias/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/minimarket/
│   │   │
│   │   ├── config/
│   │   │
│   │   ├── controller/
│   │   │
│   │   ├── entity/
│   │   │
│   │   ├── hateoas/
│   │   │
│   │   ├── repository/
│   │   │
│   │   ├── service/
│   │   │   ├── domain/
│   │   │   └── impl/
│   │   │
│   │   ├── security/
│   │   │   ├── audit/
│   │   │   ├── config/
│   │   │   ├── jwt/
│   │   │   ├── ldap/
│   │   │   ├── model/
│   │   │   ├── oauth/
│   │   │   └── service/
│   │   │
│   │   └── MinimarketApplication.java
│   │
│   └── resources/
│       ├── application.properties
│       └── ldap/
│
└── test/
    └── java/
        └── com/minimarket/
            ├── api/
            ├── controller/
            ├── entity/
            ├── security/
            ├── service/
            └── support/
```

---

# Seguridad

La aplicación incorpora múltiples mecanismos de seguridad:

- Spring Security.
- JWT Authentication.
- Control de acceso basado en roles (RBAC).
- Embedded LDAP.
- OAuth2 Resource Server.
- Protección de endpoints.
- Auditoría de eventos de seguridad.

### Roles disponibles

| Rol | Permisos |
|------|----------|
| GERENTE | Administración completa del sistema |
| EMPLEADO | Gestión de inventario y ventas |
| CLIENTE | Consulta de productos y carrito |

---

# Documentación de la API

La documentación se genera automáticamente mediante **SpringDoc OpenAPI**.

Swagger UI:

```
http://localhost:8080/swagger-ui/index.html
```

Documentación OpenAPI (JSON):

```
http://localhost:8080/v3/api-docs
```

La documentación incluye:

- Endpoints REST.
- Parámetros.
- Request Body.
- Responses.
- Modelos.
- Esquema de autenticación JWT.

---

# Implementación de HATEOAS

El proyecto incorpora Spring HATEOAS para mejorar la navegabilidad de la API.

Se implementaron assemblers para:

- Producto
- Carrito
- Inventario
- Usuario

Las respuestas incluyen enlaces dinámicos como:

- self
- productos
- inventario
- carrito
- usuarios

permitiendo descubrir recursos relacionados sin depender de URLs codificadas manualmente.

---

# Pruebas

El proyecto incorpora:

- Pruebas unitarias.
- Pruebas de integración.
- Pruebas de seguridad.
- Pruebas de controladores.
- Cobertura mediante JaCoCo.

Ejecutar todas las pruebas:

```bash
.\mvnw test
```

Generar reporte de cobertura:

```bash
.\mvnw verify
```

El reporte se genera en:

```
target/site/jacoco/index.html
```

---

# Ejecución

## Requisitos

- Java 17 o superior.

## Clonar repositorio

```bash
git clone https://github.com/USUARIO/minimarket.git
```

## Ejecutar

Windows

```bash
.\mvnw.cmd spring-boot:run
```

Linux / macOS

```bash
./mvnw spring-boot:run
```

La aplicación estará disponible en:

```
http://localhost:8080
```

---

# Características destacadas

- API REST desarrollada con Spring Boot.
- Documentación interactiva mediante Swagger UI.
- Navegación REST con Spring HATEOAS.
- Autenticación JWT.
- Control de acceso por roles.
- Arquitectura en capas.
- Cobertura de pruebas mediante JaCoCo.
- Código organizado siguiendo buenas prácticas de desarrollo.

---

# Integrantes

Proyecto desarrollado para la asignatura **Desarrollo Backend II (PBY2202)**.

- Sebastián Tapia
- Sofía Medina
- Ángel Cáceres
