package com.minimarket.service.impl;

import com.minimarket.entity.Producto;
import com.minimarket.entity.Promocion;
import com.minimarket.repository.PromocionRepository;
import com.minimarket.support.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PromocionServiceImplTest {

    @Mock
    private PromocionRepository promocionRepository;

    private PromocionServiceImpl promocionService;
    private Producto producto;

    @BeforeEach
    void setUp() {
        promocionService = new PromocionServiceImpl(promocionRepository);
        producto = TestDataFactory.producto(1L, "Bebida 1.5L", 1890.0, 30);
    }

    @Test
    void save_conDatosValidos_persistePromocion() {
        Promocion promo = promocionValida();
        when(promocionRepository.save(any(Promocion.class))).thenAnswer(inv -> inv.getArgument(0));

        Promocion saved = promocionService.save(promo);

        assertEquals("Verano Refrescante", saved.getNombre());
        assertEquals(15.0, saved.getPorcentajeDescuento());
    }

    @Test
    void save_sinProductoNiCategoria_lanzaExcepcion() {
        Promocion promo = promocionValida();
        promo.setProducto(null);
        promo.setCategoria(null);

        assertThrows(IllegalArgumentException.class, () -> promocionService.save(promo));
    }

    @Test
    void save_conDescuentoInvalido_lanzaExcepcion() {
        Promocion promo = promocionValida();
        promo.setPorcentajeDescuento(0.0);

        assertThrows(IllegalArgumentException.class, () -> promocionService.save(promo));
    }

    @Test
    void findVigentes_delegaEnRepositorio() {
        when(promocionRepository.findVigentes(any(Date.class))).thenReturn(List.of(promocionValida()));

        assertEquals(1, promocionService.findVigentes().size());
    }

    private Promocion promocionValida() {
        Promocion promo = new Promocion();
        promo.setNombre("Verano Refrescante");
        promo.setDescripcion("15% en bebidas");
        promo.setPorcentajeDescuento(15.0);
        promo.setProducto(producto);
        promo.setFechaInicio(new Date(System.currentTimeMillis() - 1000));
        promo.setFechaFin(new Date(System.currentTimeMillis() + 100_000));
        promo.setActiva(true);
        return promo;
    }
}
