package com.minimarket.service.impl;

import com.minimarket.entity.*;
import com.minimarket.repository.*;
import com.minimarket.support.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoServiceImplTest {

    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private SucursalRepository sucursalRepository;
    @Mock
    private ProductoRepository productoRepository;
    @Mock
    private StockSucursalRepository stockSucursalRepository;
    @Mock
    private PromocionRepository promocionRepository;
    @Mock
    private OrdenCompraRepository ordenCompraRepository;
    @Mock
    private ProveedorRepository proveedorRepository;

    private PedidoServiceImpl pedidoService;
    private StockSucursalServiceImpl stockSucursalService;

    private Sucursal sucursal;
    private Producto producto;
    private Proveedor proveedor;

    @BeforeEach
    void setUp() {
        OrdenCompraServiceImpl ordenCompraService = new OrdenCompraServiceImpl(
                ordenCompraRepository, proveedorRepository, sucursalRepository,
                stockSucursalRepository, null
        );
        stockSucursalService = new StockSucursalServiceImpl(
                stockSucursalRepository, sucursalRepository, productoRepository, ordenCompraService
        );
        ordenCompraService = new OrdenCompraServiceImpl(
                ordenCompraRepository, proveedorRepository, sucursalRepository,
                stockSucursalRepository, stockSucursalService
        );
        stockSucursalService = new StockSucursalServiceImpl(
                stockSucursalRepository, sucursalRepository, productoRepository, ordenCompraService
        );
        PromocionServiceImpl promocionService = new PromocionServiceImpl(promocionRepository);
        pedidoService = new PedidoServiceImpl(
                pedidoRepository, usuarioRepository, sucursalRepository, productoRepository,
                stockSucursalService, promocionService
        );

        sucursal = new Sucursal();
        sucursal.setId(1L);
        sucursal.setNombre("Sucursal Test");
        sucursal.setActiva(true);

        producto = TestDataFactory.producto(1L, "Arroz", 1590.0, 20);
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Abarrotes");
        producto.setCategoria(categoria);

        proveedor = new Proveedor();
        proveedor.setId(1L);
        proveedor.setNombre("Nestlé");
    }

    @Test
    void crearPedido_tipoRetiro_descuentaStockYCalculaTotal() {
        Usuario cliente = TestDataFactory.usuarioCompleto("cliente", "CLIENTE");
        cliente.setId(2L);

        PedidoDetalle detalle = new PedidoDetalle();
        detalle.setProducto(producto);
        detalle.setCantidad(2);

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setSucursal(sucursal);
        pedido.setTipoEntrega(Pedido.TIPO_RETIRO);
        pedido.setDetalles(List.of(detalle));

        StockSucursal stock = stockBase(20);

        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(cliente));
        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(stockSucursalRepository.findBySucursalIdAndProductoId(1L, 1L)).thenReturn(Optional.of(stock));
        when(stockSucursalRepository.save(any(StockSucursal.class))).thenAnswer(inv -> inv.getArgument(0));
        when(stockSucursalRepository.findByProductoId(1L)).thenReturn(List.of(stock));
        when(promocionRepository.findVigentes(any(Date.class))).thenReturn(List.of());
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        Pedido creado = pedidoService.crearPedido(pedido);

        assertEquals(Pedido.ESTADO_PENDIENTE, creado.getEstado());
        assertEquals(Pedido.TIPO_RETIRO, creado.getTipoEntrega());
        assertEquals(3180.0, creado.getTotal());
        assertEquals(18, stock.getCantidad());
    }

    @Test
    void crearPedido_tipoDespacho_sinDireccion_lanzaExcepcion() {
        Usuario cliente = TestDataFactory.usuarioCompleto("cliente", "CLIENTE");
        cliente.setId(2L);

        PedidoDetalle detalle = new PedidoDetalle();
        detalle.setProducto(producto);
        detalle.setCantidad(1);

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setSucursal(sucursal);
        pedido.setTipoEntrega(Pedido.TIPO_DESPACHO);
        pedido.setDetalles(List.of(detalle));

        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(cliente));
        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));

        assertThrows(IllegalArgumentException.class, () -> pedidoService.crearPedido(pedido));
    }

    @Test
    void crearPedido_sinConsentimientoDatos_lanzaExcepcion() {
        Usuario cliente = TestDataFactory.usuarioCompleto("cliente", "CLIENTE");
        cliente.setId(2L);
        cliente.setConsentimientoDatosPersonales(false);

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setSucursal(sucursal);
        pedido.setTipoEntrega(Pedido.TIPO_RETIRO);

        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(cliente));

        assertThrows(IllegalArgumentException.class, () -> pedidoService.crearPedido(pedido));
    }

    private StockSucursal stockBase(int cantidad) {
        StockSucursal stock = new StockSucursal();
        stock.setId(10L);
        stock.setSucursal(sucursal);
        stock.setProducto(producto);
        stock.setCantidad(cantidad);
        stock.setStockMinimo(5);
        stock.setProveedorPreferido(proveedor);
        return stock;
    }
}
