package com.minimarket.service.impl;

import com.minimarket.dto.RotacionProductoDto;
import com.minimarket.repository.DetalleVentaRepository;
import com.minimarket.service.ReporteService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ReporteServiceImpl implements ReporteService {

    private final DetalleVentaRepository detalleVentaRepository;

    public ReporteServiceImpl(DetalleVentaRepository detalleVentaRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
    }

    @Override
    public List<RotacionProductoDto> rotacionProductos() {
        List<Object[]> rows = detalleVentaRepository.findRotacionProductos();
        List<RotacionProductoDto> resultado = new ArrayList<>();
        for (Object[] row : rows) {
            resultado.add(new RotacionProductoDto(
                    ((Number) row[0]).longValue(),
                    (String) row[1],
                    ((Number) row[2]).longValue()
            ));
        }
        return resultado;
    }

    @Override
    public List<RotacionProductoDto> masVendidos(int limite) {
        return rotacionProductos().stream().limit(Math.max(limite, 1)).toList();
    }

    @Override
    public List<RotacionProductoDto> menosVendidos(int limite) {
        List<RotacionProductoDto> todos = new ArrayList<>(rotacionProductos());
        Collections.reverse(todos);
        return todos.stream().limit(Math.max(limite, 1)).toList();
    }
}
