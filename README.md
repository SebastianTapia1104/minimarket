# 🛒 MiniMarket Plus API

## Descripción

**MiniMarket Plus API** es una aplicación backend desarrollada con **Spring Boot 3**, cuyo objetivo es gestionar las operaciones principales de un minimarket mediante una API REST segura.

El sistema permite administrar productos, categorías, inventario, usuarios, ventas y carrito de compras, implementando buenas prácticas de desarrollo como arquitectura en capas, validación de datos, autenticación y autorización basada en roles.

Además, incorpora pruebas unitarias y de integración para garantizar la calidad del software.

---

# Objetivos del proyecto

- Desarrollar una API REST utilizando Spring Boot.
- Implementar autenticación mediante JWT.
- Aplicar autorización basada en roles utilizando Spring Security.
- Gestionar la persistencia de datos mediante Spring Data JPA.
- Incorporar pruebas unitarias e integración.
- Medir la cobertura del código utilizando JaCoCo.

---

# Funcionalidades

El sistema permite realizar las siguientes operaciones:

- Gestión de productos.
- Gestión de categorías.
- Administración del inventario.
- Gestión del carrito de compras.
- Registro de ventas.
- Administración de usuarios.
- Autenticación y autorización.
- Protección de endpoints mediante Spring Security.

---

# Tecnologías utilizadas

| Tecnología | Uso |
|------------|-----|
| Java 17 | Lenguaje de programación |
| Spring Boot 3 | Framework principal |
| Spring Security | Seguridad y autorización |
| Spring Data JPA | Persistencia de datos |
| H2 Database | Base de datos en memoria |
| JWT | Autenticación |
| Embedded LDAP | Autenticación durante el desarrollo |
| OAuth2 Resource Server | Configuración de seguridad |
| Maven Wrapper | Gestión de dependencias |
| JUnit 5 | Pruebas unitarias |
| Mockito | Simulación de dependencias |
| MockMvc | Pruebas de controladores |
| JaCoCo | Cobertura de pruebas |

---

# Arquitectura del proyecto

El proyecto sigue una arquitectura en capas para separar las responsabilidades de cada componente.

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
Base de Datos (H2)
```

Esta estructura facilita el mantenimiento, la escalabilidad y las pruebas del sistema.

---

# Estructura del proyecto

```
src
├── main
│   ├── java
│   │   └── com.minimarket
│   │       ├── config
│   │       ├── controller
│   │       ├── dto
│   │       ├── entity
│   │       ├── exception
│   │       ├── repository
│   │       ├── security
│   │       └── service
│   └── resources
│
└── test
    └── java
        └── com.minimarket
```

---

# Seguridad implementada

La aplicación incorpora diferentes mecanismos de seguridad:

- Autenticación mediante JWT.
- Spring Security.
- Control de acceso basado en roles (RBAC).
- LDAP embebido para autenticación.
- Configuración para OAuth2 Resource Server.
- Protección de endpoints.

### Roles del sistema

| Rol | Descripción |
|------|-------------|
| GERENTE | Acceso completo al sistema |
| EMPLEADO | Gestión de ventas e inventario |
| CLIENTE | Consulta de productos y compras |

---

# Requisitos

Para ejecutar el proyecto es necesario contar con:

- Java 17 o superior.
- Conexión a Internet en la primera ejecución para descargar dependencias.

No es necesario instalar Maven, ya que el proyecto utiliza **Maven Wrapper**.

---

# Ejecución del proyecto

### Windows

```bash
.\mvnw.cmd spring-boot:run
```

### Linux / macOS

```bash
./mvnw spring-boot:run
```

La aplicación quedará disponible en:

```
http://localhost:8081
```

---

# Ejecución de pruebas

Ejecutar todas las pruebas:

```bash
.\mvnw test
```

Generar el reporte de cobertura:

```bash
.\mvnw verify
```

El reporte generado por JaCoCo se encuentra en:

```
target/site/jacoco/index.html
```

---

# Pruebas implementadas

Se desarrollaron pruebas para validar:

- Entidades.
- Servicios.
- Controladores.
- Autenticación.
- Autorización por roles.
- Integración con Spring Security.

Herramientas utilizadas:

- JUnit 5
- Mockito
- Spring Boot Test
- MockMvc
- JaCoCo

---

# Cobertura

La cobertura del proyecto fue evaluada utilizando **JaCoCo**, permitiendo verificar el correcto funcionamiento de los componentes principales y de los mecanismos de seguridad implementados.

---

# Autores

Proyecto desarrollado para la asignatura **Desarrollo Backend II (PBY2202)**.

**Integrantes del equipo**

- Sebastián Tapia
- Sofía Medina
- Ángel Cáceres
