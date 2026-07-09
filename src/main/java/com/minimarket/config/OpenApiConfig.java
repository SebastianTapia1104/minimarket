package com.minimarket.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI minimarketOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Minimarket API")
                        .description("API REST para la gestión de productos, carrito de compras y ventas del minimarket.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo Minimarket")
                                .email("soporte@minimarket.cl")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Token JWT obtenido desde POST /api/auth/login"))
                        .addSchemas("ProductoRequest", productoRequestSchema())
                        .addSchemas("ProductoResponse", productoResponseSchema())
                        .addSchemas("CarritoRequest", carritoRequestSchema())
                        .addSchemas("CarritoResponse", carritoResponseSchema())
                        .addSchemas("InventarioRequest", inventarioRequestSchema())
                        .addSchemas("InventarioResponse", inventarioResponseSchema())
                        .addSchemas("UsuarioRequest", usuarioRequestSchema())
                        .addSchemas("UsuarioResponse", usuarioResponseSchema())
                        .addSchemas("ErrorResponse", errorResponseSchema()));
    }

    private Schema<?> productoRequestSchema() {
        return new Schema<>()
                .type("object")
                .description("Datos para crear o actualizar un producto")
                .required(java.util.List.of("nombre", "precio", "stock", "categoria"))
                .properties(Map.of(
                        "nombre", new Schema<>().type("string").example("Arroz 1kg"),
                        "precio", new Schema<>().type("number").format("double").example(1590.0),
                        "stock", new Schema<>().type("integer").example(50),
                        "categoria", new Schema<>().type("object").addProperty("id", new Schema<>().type("integer").example(1))
                ));
    }

    private Schema<?> productoResponseSchema() {
        return new Schema<>()
                .type("object")
                .description("Producto registrado en el sistema")
                .properties(Map.of(
                        "id", new Schema<>().type("integer").format("int64").example(1),
                        "nombre", new Schema<>().type("string").example("Arroz 1kg"),
                        "precio", new Schema<>().type("number").format("double").example(1590.0),
                        "stock", new Schema<>().type("integer").example(50),
                        "categoria", new Schema<>().type("object").addProperty("id", new Schema<>().type("integer").example(1))
                                .addProperty("nombre", new Schema<>().type("string").example("Abarrotes"))
                ));
    }

    private Schema<?> carritoRequestSchema() {
        return new Schema<>()
                .type("object")
                .description("Datos para agregar un producto al carrito")
                .required(java.util.List.of("usuario", "producto", "cantidad"))
                .properties(Map.of(
                        "usuario", new Schema<>().type("object").addProperty("id", new Schema<>().type("integer").example(1)),
                        "producto", new Schema<>().type("object").addProperty("id", new Schema<>().type("integer").example(2)),
                        "cantidad", new Schema<>().type("integer").example(3)
                ));
    }

    private Schema<?> carritoResponseSchema() {
        return new Schema<>()
                .type("object")
                .description("Ítem del carrito de compras")
                .properties(Map.of(
                        "id", new Schema<>().type("integer").format("int64").example(1),
                        "usuario", new Schema<>().type("object").addProperty("id", new Schema<>().type("integer").example(1)),
                        "producto", new Schema<>().type("object")
                                .addProperty("id", new Schema<>().type("integer").example(2))
                                .addProperty("nombre", new Schema<>().type("string").example("Leche entera 1L"))
                                .addProperty("precio", new Schema<>().type("number").format("double").example(990.0)),
                        "cantidad", new Schema<>().type("integer").example(3)
                ));
    }

    private Schema<?> errorResponseSchema() {
        return new Schema<>()
                .type("object")
                .description("Respuesta de error de la API")
                .properties(Map.of(
                        "timestamp", new Schema<>().type("string").format("date-time"),
                        "status", new Schema<>().type("integer").example(404),
                        "error", new Schema<>().type("string").example("Not Found"),
                        "message", new Schema<>().type("string").example("Recurso no encontrado")
                ));
    }

    private Schema<?> inventarioRequestSchema() {
        return new Schema<>()
                .type("object")
                .description("Datos para registrar un movimiento de inventario")
                .required(java.util.List.of("producto", "cantidad", "tipoMovimiento", "fechaMovimiento"))
                .properties(Map.of(
                        "producto", new Schema<>().type("object").addProperty("id", new Schema<>().type("integer").example(1)),
                        "cantidad", new Schema<>().type("integer").example(10),
                        "tipoMovimiento", new Schema<>().type("string").example("Entrada"),
                        "fechaMovimiento", new Schema<>().type("string").format("date-time")
                ));
    }

    private Schema<?> inventarioResponseSchema() {
        return new Schema<>()
                .type("object")
                .description("Movimiento de inventario registrado")
                .properties(Map.of(
                        "id", new Schema<>().type("integer").format("int64").example(1),
                        "producto", new Schema<>().type("object").addProperty("id", new Schema<>().type("integer").example(1)),
                        "cantidad", new Schema<>().type("integer").example(10),
                        "tipoMovimiento", new Schema<>().type("string").example("Entrada"),
                        "fechaMovimiento", new Schema<>().type("string").format("date-time")
                ));
    }

    private Schema<?> usuarioRequestSchema() {
        return new Schema<>()
                .type("object")
                .description("Datos para registrar un usuario")
                .required(java.util.List.of("username", "nombre", "apellido", "email", "direccion", "password"))
                .properties(Map.of(
                        "username", new Schema<>().type("string").example("nuevo_cliente"),
                        "nombre", new Schema<>().type("string").example("Ana"),
                        "apellido", new Schema<>().type("string").example("Pérez"),
                        "email", new Schema<>().type("string").example("ana@minimarket.cl"),
                        "direccion", new Schema<>().type("string").example("Av. Central 456"),
                        "password", new Schema<>().type("string").example("Cliente123!")
                ));
    }

    private Schema<?> usuarioResponseSchema() {
        return new Schema<>()
                .type("object")
                .description("Usuario registrado en el sistema")
                .properties(Map.of(
                        "id", new Schema<>().type("integer").format("int64").example(1),
                        "username", new Schema<>().type("string").example("cliente"),
                        "nombre", new Schema<>().type("string").example("Cliente"),
                        "apellido", new Schema<>().type("string").example("Demo"),
                        "email", new Schema<>().type("string").example("cliente@minimarket.cl"),
                        "direccion", new Schema<>().type("string").example("Av. Principal 123")
                ));
    }
}
