package com.minimarket.service;

import com.minimarket.dto.RotacionProductoDto;

import java.util.List;

public interface ReporteService {
    List<RotacionProductoDto> rotacionProductos();

    List<RotacionProductoDto> masVendidos(int limite);

    List<RotacionProductoDto> menosVendidos(int limite);
}
