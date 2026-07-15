package com.minimarket.service.impl;

import com.minimarket.dto.RotacionProductoDto;
import com.minimarket.repository.DetalleVentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReporteServiceImplTest {

    @Mock
    private DetalleVentaRepository detalleVentaRepository;

    private ReporteServiceImpl reporteService;

    @BeforeEach
    void setUp() {
        reporteService = new ReporteServiceImpl(detalleVentaRepository);
    }

    @Test
    void rotacionProductos_mapeaResultadosDelRepositorio() {
        when(detalleVentaRepository.findRotacionProductos()).thenReturn(List.of(
                new Object[]{1L, "Arroz", 50L},
                new Object[]{2L, "Leche", 10L}
        ));

        List<RotacionProductoDto> rotacion = reporteService.rotacionProductos();

        assertEquals(2, rotacion.size());
        assertEquals("Arroz", rotacion.get(0).nombreProducto());
        assertEquals(50L, rotacion.get(0).unidadesVendidas());
    }

    @Test
    void masVendidos_retornaPrimerosSegunLimite() {
        when(detalleVentaRepository.findRotacionProductos()).thenReturn(List.of(
                new Object[]{1L, "Arroz", 50L},
                new Object[]{2L, "Leche", 10L},
                new Object[]{3L, "Pan", 5L}
        ));

        List<RotacionProductoDto> top = reporteService.masVendidos(1);

        assertEquals(1, top.size());
        assertEquals("Arroz", top.get(0).nombreProducto());
    }

    @Test
    void menosVendidos_retornaUltimosSegunLimite() {
        when(detalleVentaRepository.findRotacionProductos()).thenReturn(List.of(
                new Object[]{1L, "Arroz", 50L},
                new Object[]{2L, "Leche", 10L}
        ));

        List<RotacionProductoDto> bottom = reporteService.menosVendidos(1);

        assertEquals(1, bottom.size());
        assertEquals("Leche", bottom.get(0).nombreProducto());
    }
}
