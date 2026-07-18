# 🛒 MiniMarket Plus API

## 📋 Descripción

**MiniMarket Plus API** es una aplicación backend desarrollada con **Java 17 y Spring Boot**, orientada a la gestión integral de las operaciones de una cadena de minimarkets.

El sistema implementa una API REST que permite administrar productos, categorías, inventario, sucursales, stock por sucursal, proveedores, órdenes de compra, ventas, pedidos en línea, promociones, usuarios y reportes.

La aplicación incorpora mecanismos de seguridad mediante **Spring Security**, autenticación basada en **JWT**, autorización mediante roles (**RBAC**), autenticación **LDAP** y soporte opcional para **OAuth2 / IDaaS**.

Además, utiliza **Spring HATEOAS** para incorporar enlaces hipermedia en las respuestas y **OpenAPI / Swagger UI** para documentar y explorar los endpoints disponibles.

El proyecto fue desarrollado como parte de la **Evaluación Final Transversal de la asignatura Desarrollo Backend II**.

---

## 🎯 Objetivo del proyecto

El objetivo de MiniMarket Plus es proporcionar una solución backend capaz de centralizar y gestionar las principales operaciones de un minimarket, incluyendo:

- Administración del catálogo de productos.
- Gestión centralizada del inventario.
- Control de stock por sucursal.
- Gestión de proveedores.
- Generación manual y automática de órdenes de compra.
- Registro de ventas presenciales.
- Gestión de pedidos en línea.
- Modalidades de retiro y despacho.
- Administración de promociones y descuentos.
- Gestión de usuarios y roles organizacionales.
- Generación de reportes de rotación de productos.
- Protección de datos personales.
- Seguridad mediante autenticación y autorización.
- Documentación interactiva de la API.
- Pruebas automatizadas y análisis de cobertura.

---

# 🚀 Funcionalidades principales

## 📦 Gestión de productos y categorías

Permite administrar el catálogo centralizado de productos del minimarket.

Incluye operaciones para:

- Crear productos.
- Consultar productos.
- Actualizar productos.
- Eliminar productos.
- Gestionar categorías.
- Relacionar productos con categorías.

---

## 🏪 Gestión de sucursales

El sistema permite administrar diferentes sucursales del minimarket.

Cada sucursal puede mantener su propio nivel de stock, permitiendo consultar la disponibilidad de productos de forma independiente.

---

## 📊 Inventario y stock por sucursal

La solución implementa gestión de inventario tanto a nivel general como por sucursal.

Entre sus funcionalidades se encuentran:

- Registro de movimientos de inventario.
- Control de entradas y salidas.
- Consulta de stock.
- Consulta de disponibilidad por producto.
- Consulta de disponibilidad por sucursal.
- Validación de stock mínimo.
- Sincronización con operaciones de ventas y pedidos.

---

## 🚚 Gestión de proveedores

Permite registrar y administrar proveedores asociados a los productos del minimarket.

Los proveedores son utilizados por el sistema para gestionar las órdenes de compra y el reabastecimiento del inventario.

---

## 📑 Órdenes de compra

El sistema permite gestionar órdenes de compra asociadas a proveedores.

Las órdenes pueden generarse:

- Manualmente.
- Automáticamente cuando el stock de un producto alcanza o baja del nivel mínimo configurado.

Al recibir una orden de compra, el stock correspondiente puede ser actualizado automáticamente.

---

## 🛒 Ventas

Permite registrar las ventas realizadas en el minimarket.

El proceso considera:

- Productos vendidos.
- Cantidades.
- Validación de stock disponible.
- Cálculo de totales.
- Actualización del inventario.

El acceso a las operaciones de venta se encuentra protegido mediante roles.

---

## 📦 Pedidos en línea

El sistema incorpora gestión de pedidos realizados por clientes.

Se soportan dos modalidades de entrega:

- **RETIRO:** el cliente retira el pedido en una sucursal.
- **DESPACHO:** el pedido es enviado a una dirección especificada.

Durante el procesamiento del pedido se realizan validaciones de:

- Stock disponible.
- Sucursal seleccionada.
- Dirección de despacho.
- Consentimiento para el tratamiento de datos personales.
- Promociones vigentes.

---

## 🏷️ Promociones y descuentos

MiniMarket Plus permite administrar promociones asociadas a productos o categorías.

Las promociones consideran:

- Porcentaje de descuento.
- Fecha de inicio.
- Fecha de término.
- Estado activo o inactivo.
- Producto o categoría asociada.

Durante la creación de pedidos, el sistema puede aplicar automáticamente el mejor descuento vigente disponible.

---

## 📈 Reportes

El sistema incorpora funcionalidades de análisis sobre las ventas registradas.

Entre los reportes disponibles se encuentran:

- Productos más vendidos.
- Productos menos vendidos.
- Análisis de rotación de productos.

El acceso a estos reportes está restringido a roles administrativos.

---

# 🔐 Seguridad

La aplicación incorpora múltiples mecanismos de seguridad:

- Spring Security.
- Autenticación JWT.
- Autorización basada en roles (RBAC).
- Protección de endpoints.
- Seguridad a nivel de método mediante `@PreAuthorize`.
- Contraseñas almacenadas utilizando BCrypt.
- Autenticación LDAP embebida.
- Soporte opcional para OAuth2 / IDaaS.
- OAuth2 Resource Server.
- Auditoría de eventos de seguridad.
- Manejo personalizado de errores 401 y 403.
- Configuración de headers de seguridad.

La API utiliza una arquitectura de sesión **STATELESS** para el flujo principal basado en JWT.

---

# 👥 Roles del sistema

Los roles representan los distintos perfiles definidos dentro de la organización de MiniMarket Plus.

| Rol | Perfil |
|-----|--------|
| `GERENTE` | Administración general y acceso a funcionalidades críticas |
| `JEFE_TURNO` | Supervisión de operaciones, inventario y reportes |
| `REPONEDOR` | Gestión relacionada con inventario y reposición |
| `CAJERO` | Registro y gestión de ventas |
| `ASISTENTE_CLIENTE` | Apoyo en operaciones relacionadas con clientes y pedidos |
| `CLIENTE` | Consulta de productos y realización de pedidos |
| `EMPLEADO` | Rol mantenido por compatibilidad con versiones anteriores |

La autorización se implementa tanto en la configuración HTTP de Spring Security como mediante anotaciones `@PreAuthorize`.

---

# 🔑 Autenticación JWT

El flujo principal de autenticación utiliza **JSON Web Tokens (JWT)**.

### Flujo de autenticación

1. El usuario envía sus credenciales al endpoint de autenticación.
2. Spring Security valida las credenciales.
3. El sistema genera un token JWT.
4. El cliente utiliza el token en las solicitudes protegidas.
5. `JwtAuthenticationFilter` valida el token y restaura el contexto de seguridad.

Para acceder a un recurso protegido, el token debe enviarse mediante el header:

```http
Authorization: Bearer <token>
```

### Endpoints principales de autenticación

```text
POST /api/auth/register
POST /api/auth/login
POST /api/auth/ldap/login
```

---

# 🏢 Autenticación LDAP

El proyecto incorpora soporte para autenticación mediante LDAP embebido.

Los principales componentes utilizados son:

```text
EmbeddedLdapServerConfig
LdapSecurityConfig
LdapAuthService
```

El servidor LDAP embebido utiliza datos definidos en:

```text
src/main/resources/ldap/users.ldif
```

Cuando un usuario se autentica mediante LDAP, el sistema puede provisionar el usuario localmente y generar un JWT para utilizar el resto de la API.

---

# 🌐 OAuth2 / IDaaS

El proyecto incluye soporte opcional para:

- OAuth2 Client.
- OAuth2 Login.
- OAuth2 Resource Server.
- Validación de JWT externos.

Esta funcionalidad se encuentra desacoplada del flujo JWT principal y puede habilitarse mediante configuración.

---

# 🔒 Protección de datos personales

La aplicación incorpora medidas orientadas a la protección de datos personales:

- Contraseñas almacenadas mediante BCrypt.
- Exclusión del campo `password` de las respuestas JSON.
- Consentimiento de tratamiento de datos en operaciones que lo requieren.
- Endpoint público con información sobre la política de privacidad.
- Restricción de acceso a información administrativa.
- Auditoría de eventos de autenticación y seguridad.

---

# 🏗️ Arquitectura

El proyecto utiliza una arquitectura organizada en capas.

```text
                Cliente / Swagger / Frontend
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

De forma transversal se integran los componentes de seguridad, documentación e hipermedia:

```text
Spring Security
      │
      ├── JWT
      ├── RBAC
      ├── LDAP
      ├── OAuth2
      └── Auditoría

Spring HATEOAS
      │
      └── Enlaces hipermedia

OpenAPI / Swagger
      │
      └── Documentación interactiva
```

El backend también se encuentra organizado conceptualmente en contextos funcionales o microservicios lógicos:

- Seguridad.
- Catálogo.
- Inventario.
- Ventas.
- Promociones.
- Reportes.
- Usuarios.

Estos contextos forman parte de una única aplicación Spring Boot y permiten separar las responsabilidades funcionales del sistema.

---

# 📂 Estructura general del proyecto

```text
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
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/minimarket/
    │   │       │
    │   │       ├── config/
    │   │       │   ├── DomainDataInitializer
    │   │       │   ├── GlobalExceptionHandler
    │   │       │   ├── OpenApiConfig
    │   │       │   └── SecurityDataInitializer
    │   │       │
    │   │       ├── controller/
    │   │       │   ├── AuthController
    │   │       │   ├── CarritoController
    │   │       │   ├── CategoriaController
    │   │       │   ├── InventarioController
    │   │       │   ├── OrdenCompraController
    │   │       │   ├── PedidoController
    │   │       │   ├── PrivacidadController
    │   │       │   ├── ProductoController
    │   │       │   ├── PromocionController
    │   │       │   ├── ProveedorController
    │   │       │   ├── ReporteController
    │   │       │   ├── StockSucursalController
    │   │       │   ├── SucursalController
    │   │       │   ├── UsuarioController
    │   │       │   └── VentaController
    │   │       │
    │   │       ├── dto/
    │   │       │
    │   │       ├── entity/
    │   │       │
    │   │       ├── hateoas/
    │   │       │
    │   │       ├── repository/
    │   │       │
    │   │       ├── security/
    │   │       │   ├── audit/
    │   │       │   ├── config/
    │   │       │   ├── jwt/
    │   │       │   ├── ldap/
    │   │       │   ├── model/
    │   │       │   ├── oauth/
    │   │       │   └── service/
    │   │       │
    │   │       └── service/
    │   │           ├── domain/
    │   │           └── impl/
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

# 🧰 Tecnologías utilizadas

| Tecnología | Uso |
|------------|-----|
| Java 17 | Lenguaje de programación |
| Spring Boot | Framework principal |
| Spring Web | Desarrollo de API REST |
| Spring Data JPA | Persistencia de datos |
| Spring Security | Autenticación y autorización |
| JJWT | Generación y validación de JWT |
| BCrypt | Hash seguro de contraseñas |
| Spring LDAP | Integración LDAP |
| UnboundID LDAP SDK | Servidor LDAP embebido |
| OAuth2 Client | Autenticación OAuth2 |
| OAuth2 Resource Server | Validación de tokens externos |
| Spring HATEOAS | Hipermedia REST |
| SpringDoc OpenAPI | Documentación de la API |
| Swagger UI | Exploración interactiva de endpoints |
| H2 Database | Base de datos en memoria |
| Maven | Gestión de dependencias y construcción |
| JUnit 5 | Pruebas automatizadas |
| Mockito | Pruebas unitarias con mocks |
| MockMvc | Pruebas de controladores |
| Spring Security Test | Pruebas de autenticación y autorización |
| JaCoCo | Medición de cobertura |

---

# 🔗 HATEOAS

El proyecto utiliza **Spring HATEOAS** para incorporar enlaces dinámicos en las respuestas REST.

Se implementaron assemblers para recursos como:

- Producto.
- Inventario.
- Carrito.
- Usuario.
- Sucursal.

Además, otros controladores utilizan `EntityModel`, `CollectionModel` y `linkTo(methodOn(...))` para construir relaciones entre recursos.

Ejemplo conceptual de una respuesta HATEOAS:

```json
{
  "id": 1,
  "nombre": "Producto ejemplo",
  "_links": {
    "self": {
      "href": "http://localhost:8080/api/productos/1"
    },
    "productos": {
      "href": "http://localhost:8080/api/productos"
    }
  }
}
```

---

# 📖 Documentación OpenAPI y Swagger

La API se documenta utilizando **SpringDoc OpenAPI**.

Una vez iniciada la aplicación, la documentación puede consultarse desde Swagger UI.

### Swagger UI

```text
http://localhost:8080/swagger-ui.html
```

También puede estar disponible mediante:

```text
http://localhost:8080/swagger-ui/index.html
```

### OpenAPI JSON

```text
http://localhost:8080/v3/api-docs
```

La documentación incluye:

- Endpoints REST.
- Métodos HTTP.
- Parámetros.
- Request Body.
- Responses.
- Modelos de datos.
- Esquema Bearer JWT.
- Operaciones protegidas mediante autenticación.

---

# 🧪 Pruebas automatizadas

El proyecto cuenta con pruebas orientadas a diferentes niveles de la aplicación:

- Entidades y dominio.
- Servicios de negocio.
- Controladores.
- Seguridad.
- Autenticación JWT.
- Autorización basada en roles.
- LDAP.
- OAuth2.
- Integración.
- Inventario y stock.
- Órdenes de compra.
- Pedidos.
- Promociones.
- Reportes.
- Privacidad.

Las principales herramientas utilizadas son:

```text
JUnit 5
Mockito
MockMvc
Spring Security Test
JaCoCo
```

## Ejecutar pruebas

### Windows

```bash
.\mvnw.cmd test
```

### Linux / macOS

```bash
./mvnw test
```

---

## Ejecutar pruebas y verificar cobertura

### Windows

```bash
.\mvnw.cmd verify
```

### Linux / macOS

```bash
./mvnw verify
```

El reporte HTML de cobertura se genera en:

```text
target/site/jacoco/index.html
```

### Resultados obtenidos

```text
Pruebas ejecutadas: 221
Fallos: 0
Errores: 0
Pruebas omitidas: 0
Cobertura de instrucciones: 85 %
Cobertura mínima configurada: 80 %
```

---

# ▶️ Ejecución del proyecto

## Requisitos

Antes de ejecutar el proyecto se requiere:

- Java 17 o superior.
- Maven o Maven Wrapper incluido en el proyecto.
- Git, en caso de clonar el repositorio.

---

## Clonar el repositorio

```bash
git clone <URL_DEL_REPOSITORIO>
cd minimarket
```

> Reemplazar `<URL_DEL_REPOSITORIO>` por la URL correspondiente al repositorio de GitHub.

---

## Ejecutar en Windows

```bash
.\mvnw.cmd spring-boot:run
```

## Ejecutar en Linux / macOS

```bash
./mvnw spring-boot:run
```

Una vez iniciada correctamente, la aplicación estará disponible en:

```text
http://localhost:8080
```

---

# 🔐 Uso de endpoints protegidos

Para acceder a endpoints protegidos:

1. Autenticarse utilizando `/api/auth/login`.
2. Obtener el token JWT generado por la aplicación.
3. Enviar el token en el header `Authorization`.

```http
Authorization: Bearer <token>
```

En Swagger UI se puede utilizar el botón **Authorize** e ingresar el token correspondiente para probar los endpoints protegidos.

---

# ⭐ Características destacadas

- API REST desarrollada con Spring Boot.
- Arquitectura organizada por capas y dominios funcionales.
- Gestión centralizada de productos e inventario.
- Control de stock independiente por sucursal.
- Sistema de proveedores y órdenes de compra.
- Generación automática de órdenes de compra por stock mínimo.
- Gestión de ventas presenciales.
- Pedidos online con retiro o despacho.
- Sistema de promociones y descuentos.
- Reportes de rotación de productos.
- Gestión de usuarios según organigrama.
- Autenticación JWT.
- Autorización basada en roles (RBAC).
- Integración LDAP.
- Soporte opcional para OAuth2 / IDaaS.
- Auditoría de seguridad.
- Protección de datos personales.
- API documentada mediante OpenAPI y Swagger.
- Navegación hipermedia mediante HATEOAS.
- 221 pruebas automatizadas ejecutadas exitosamente.
- 85 % de cobertura de instrucciones con JaCoCo.

---

# 👨‍💻 Integrantes

Proyecto desarrollado para la asignatura **Desarrollo Backend II**.

- Sebastián Tapia
- Sofía Medina
- Ángel Cáceres
