package com.minimarket.dto;

public record RotacionProductoDto(
        Long productoId,
        String nombreProducto,
        Long unidadesVendidas
) {
}
