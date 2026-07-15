package com.minimarket.dto;

public record DisponibilidadProductoDto(
        Long productoId,
        String nombreProducto,
        Long sucursalId,
        String nombreSucursal,
        Integer cantidadDisponible,
        boolean disponible
) {
}
