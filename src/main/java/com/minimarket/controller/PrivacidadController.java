package com.minimarket.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/privacidad")
@Tag(name = "Privacidad", description = "Cumplimiento de protección de datos personales (Chile)")
public class PrivacidadController {

    @GetMapping("/politica")
    @Operation(summary = "Política de tratamiento de datos personales")
    public Map<String, Object> politica() {
        return Map.of(
                "titulo", "Política de Protección de Datos Personales - MiniMarket Plus",
                "normativa", List.of("Ley N° 19.628", "Ley N° 21.719"),
                "responsable", "MiniMarket Plus SpA",
                "finalidades", List.of(
                        "Registro y autenticación de usuarios",
                        "Procesamiento de pedidos y ventas",
                        "Gestión de entregas a domicilio",
                        "Seguridad y auditoría de accesos"
                ),
                "derechos", List.of(
                        "Acceso", "Rectificación", "Cancelación", "Oposición", "Portabilidad"
                ),
                "contacto", "privacidad@minimarketplus.cl",
                "retencion", "Los datos se conservan mientras exista relación comercial o obligación legal"
        );
    }

    @GetMapping("/microservicios")
    @Operation(summary = "Mapa de microservicios lógicos del backend")
    public List<Map<String, String>> microservicios() {
        return List.of(
                Map.of("nombre", "ms-seguridad", "basePath", "/api/auth", "responsabilidad", "Registro, login JWT y autenticación"),
                Map.of("nombre", "ms-catalogo", "basePath", "/api/productos,/api/categorias,/api/sucursales", "responsabilidad", "Catálogo y sucursales"),
                Map.of("nombre", "ms-inventario", "basePath", "/api/inventario,/api/stock-sucursal,/api/ordenes-compra", "responsabilidad", "Stock multi-sucursal y reposición"),
                Map.of("nombre", "ms-ventas", "basePath", "/api/ventas,/api/carrito,/api/pedidos", "responsabilidad", "Ventas POS y pedidos en línea"),
                Map.of("nombre", "ms-promociones", "basePath", "/api/promociones", "responsabilidad", "Ofertas centralizadas"),
                Map.of("nombre", "ms-reportes", "basePath", "/api/reportes", "responsabilidad", "Rotación y analítica de ventas"),
                Map.of("nombre", "ms-usuarios", "basePath", "/api/usuarios,/api/privacidad", "responsabilidad", "Usuarios, roles y privacidad")
        );
    }
}
