package com.minimarket.service.impl;

import com.minimarket.entity.*;
import com.minimarket.repository.PedidoRepository;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.repository.SucursalRepository;
import com.minimarket.repository.UsuarioRepository;
import com.minimarket.service.PedidoService;
import com.minimarket.service.PromocionService;
import com.minimarket.service.StockSucursalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SucursalRepository sucursalRepository;
    private final ProductoRepository productoRepository;
    private final StockSucursalService stockSucursalService;
    private final PromocionService promocionService;

    public PedidoServiceImpl(
            PedidoRepository pedidoRepository,
            UsuarioRepository usuarioRepository,
            SucursalRepository sucursalRepository,
            ProductoRepository productoRepository,
            StockSucursalService stockSucursalService,
            PromocionService promocionService
    ) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.sucursalRepository = sucursalRepository;
        this.productoRepository = productoRepository;
        this.stockSucursalService = stockSucursalService;
        this.promocionService = promocionService;
    }

    @Override
    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    @Override
    public Pedido findById(Long id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    @Override
    public List<Pedido> findByClienteId(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    @Override
    @Transactional
    public Pedido crearPedido(Pedido pedido) {
        Usuario cliente = usuarioRepository.findById(pedido.getCliente().getId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        if (!cliente.isConsentimientoDatosPersonales()) {
            throw new IllegalArgumentException(
                    "El cliente debe aceptar el tratamiento de datos personales para realizar pedidos");
        }
        Sucursal sucursal = sucursalRepository.findById(pedido.getSucursal().getId())
                .orElseThrow(() -> new IllegalArgumentException("Sucursal no encontrada"));
        if (!sucursal.isActiva()) {
            throw new IllegalArgumentException("La sucursal no se encuentra operativa");
        }
        if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe incluir al menos un producto");
        }

        pedido.setCliente(cliente);
        pedido.setSucursal(sucursal);
        pedido.validarTipoEntrega();
        pedido.setFechaCreacion(new Date());
        pedido.setEstado(Pedido.ESTADO_PENDIENTE);

        List<Promocion> vigentes = promocionService.findVigentes();
        double total = 0.0;

        for (PedidoDetalle detalle : pedido.getDetalles()) {
            Producto producto = productoRepository.findById(detalle.getProducto().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
            if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                throw new IllegalArgumentException("Cantidad inválida en el detalle del pedido");
            }
            stockSucursalService.ajustarCantidad(sucursal.getId(), producto.getId(), -detalle.getCantidad());

            double precio = aplicarDescuento(producto, vigentes);
            detalle.setProducto(producto);
            detalle.setPrecioUnitario(precio);
            detalle.setPedido(pedido);
            total += detalle.subtotal();
        }

        pedido.setTotal(total);
        return pedidoRepository.save(pedido);
    }

    @Override
    @Transactional
    public Pedido actualizarEstado(Long id, String estado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));
        pedido.setEstado(estado);
        return pedidoRepository.save(pedido);
    }

    private double aplicarDescuento(Producto producto, List<Promocion> vigentes) {
        double precio = producto.getPrecio();
        double mejorDescuento = 0.0;
        for (Promocion promo : vigentes) {
            boolean aplicaProducto = promo.getProducto() != null
                    && producto.getId().equals(promo.getProducto().getId());
            boolean aplicaCategoria = promo.getCategoria() != null
                    && producto.getCategoria() != null
                    && producto.getCategoria().getId().equals(promo.getCategoria().getId());
            if (aplicaProducto || aplicaCategoria) {
                mejorDescuento = Math.max(mejorDescuento, promo.getPorcentajeDescuento());
            }
        }
        return precio * (1 - mejorDescuento / 100.0);
    }
}
