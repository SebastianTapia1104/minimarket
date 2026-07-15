package com.minimarket.service.impl;

import com.minimarket.entity.*;
import com.minimarket.repository.OrdenCompraRepository;
import com.minimarket.repository.ProveedorRepository;
import com.minimarket.repository.StockSucursalRepository;
import com.minimarket.repository.SucursalRepository;
import com.minimarket.support.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrdenCompraServiceImplTest {

    @Mock
    private OrdenCompraRepository ordenCompraRepository;
    @Mock
    private ProveedorRepository proveedorRepository;
    @Mock
    private SucursalRepository sucursalRepository;
    @Mock
    private StockSucursalRepository stockSucursalRepository;
    @Mock
    private StockSucursalServiceImpl stockSucursalService;

    private OrdenCompraServiceImpl ordenCompraService;

    private Sucursal sucursal;
    private Producto producto;
    private Proveedor proveedor;

    @BeforeEach
    void setUp() {
        ordenCompraService = new OrdenCompraServiceImpl(
                ordenCompraRepository,
                proveedorRepository,
                sucursalRepository,
                stockSucursalRepository,
                stockSucursalService
        );

        sucursal = new Sucursal();
        sucursal.setId(1L);
        sucursal.setNombre("Sucursal Test");
        sucursal.setActiva(true);

        producto = TestDataFactory.producto(1L, "Arroz", 1590.0, 20);

        proveedor = new Proveedor();
        proveedor.setId(1L);
        proveedor.setNombre("Nestlé");
    }

    @Test
    void generarAutomaticaSiCorresponde_cuandoStockBajoMinimo_creaOrdenPendiente() {
        StockSucursal stock = new StockSucursal();
        stock.setSucursal(sucursal);
        stock.setProducto(producto);
        stock.setCantidad(5);
        stock.setStockMinimo(10);
        stock.setProveedorPreferido(proveedor);

        when(ordenCompraRepository.existsPendientePorProducto(1L, 1L)).thenReturn(false);
        when(ordenCompraRepository.save(any(OrdenCompra.class))).thenAnswer(inv -> inv.getArgument(0));

        OrdenCompra orden = ordenCompraService.generarAutomaticaSiCorresponde(stock);

        assertNotNull(orden);
        assertTrue(orden.isGeneradaAutomaticamente());
        assertEquals(OrdenCompra.ESTADO_PENDIENTE, orden.getEstado());
        assertEquals(1, orden.getDetalles().size());
        assertEquals(producto.getId(), orden.getDetalles().get(0).getProducto().getId());
    }

    @Test
    void generarAutomaticaSiCorresponde_cuandoStockSobreMinimo_noCreaOrden() {
        StockSucursal stock = new StockSucursal();
        stock.setSucursal(sucursal);
        stock.setProducto(producto);
        stock.setCantidad(20);
        stock.setStockMinimo(10);
        stock.setProveedorPreferido(proveedor);

        assertNull(ordenCompraService.generarAutomaticaSiCorresponde(stock));
    }

    @Test
    void generarAutomaticaSiCorresponde_cuandoYaExisteOrdenPendiente_noDuplica() {
        StockSucursal stock = new StockSucursal();
        stock.setSucursal(sucursal);
        stock.setProducto(producto);
        stock.setCantidad(3);
        stock.setStockMinimo(10);
        stock.setProveedorPreferido(proveedor);

        when(ordenCompraRepository.existsPendientePorProducto(1L, 1L)).thenReturn(true);

        assertNull(ordenCompraService.generarAutomaticaSiCorresponde(stock));
    }
}
