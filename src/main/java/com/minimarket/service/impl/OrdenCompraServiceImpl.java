package com.minimarket.service.impl;

import com.minimarket.entity.*;
import com.minimarket.repository.OrdenCompraRepository;
import com.minimarket.repository.ProveedorRepository;
import com.minimarket.repository.StockSucursalRepository;
import com.minimarket.repository.SucursalRepository;
import com.minimarket.service.OrdenCompraService;
import com.minimarket.service.StockSucursalService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrdenCompraServiceImpl implements OrdenCompraService {

    private final OrdenCompraRepository ordenCompraRepository;
    private final ProveedorRepository proveedorRepository;
    private final SucursalRepository sucursalRepository;
    private final StockSucursalRepository stockSucursalRepository;
    private final StockSucursalService stockSucursalService;

    public OrdenCompraServiceImpl(
            OrdenCompraRepository ordenCompraRepository,
            ProveedorRepository proveedorRepository,
            SucursalRepository sucursalRepository,
            StockSucursalRepository stockSucursalRepository,
            @Lazy StockSucursalService stockSucursalService
    ) {
        this.ordenCompraRepository = ordenCompraRepository;
        this.proveedorRepository = proveedorRepository;
        this.sucursalRepository = sucursalRepository;
        this.stockSucursalRepository = stockSucursalRepository;
        this.stockSucursalService = stockSucursalService;
    }

    @Override
    public List<OrdenCompra> findAll() {
        return ordenCompraRepository.findAll();
    }

    @Override
    public OrdenCompra findById(Long id) {
        return ordenCompraRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public OrdenCompra crearManual(OrdenCompra orden) {
        Proveedor proveedor = proveedorRepository.findById(orden.getProveedor().getId())
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));
        Sucursal sucursal = sucursalRepository.findById(orden.getSucursal().getId())
                .orElseThrow(() -> new IllegalArgumentException("Sucursal no encontrada"));
        if (orden.getDetalles() == null || orden.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("La orden de compra debe incluir al menos un producto");
        }
        orden.setProveedor(proveedor);
        orden.setSucursal(sucursal);
        orden.setFechaCreacion(new Date());
        orden.setEstado(OrdenCompra.ESTADO_PENDIENTE);
        orden.setGeneradaAutomaticamente(false);
        for (OrdenCompraDetalle detalle : orden.getDetalles()) {
            detalle.setOrdenCompra(orden);
            if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                throw new IllegalArgumentException("La cantidad del detalle debe ser mayor a cero");
            }
        }
        return ordenCompraRepository.save(orden);
    }

    @Override
    @Transactional
    public OrdenCompra generarAutomaticaSiCorresponde(StockSucursal stock) {
        if (stock == null || !stock.estaBajoMinimo()) {
            return null;
        }
        if (stock.getProveedorPreferido() == null || stock.getProveedorPreferido().getId() == null) {
            return null;
        }
        Long sucursalId = stock.getSucursal().getId();
        Long productoId = stock.getProducto().getId();
        if (ordenCompraRepository.existsPendientePorProducto(sucursalId, productoId)) {
            return null;
        }

        int cantidadReponer = Math.max(stock.getStockMinimo() * 2 - stock.getCantidad(), stock.getStockMinimo());

        OrdenCompra orden = new OrdenCompra();
        orden.setProveedor(stock.getProveedorPreferido());
        orden.setSucursal(stock.getSucursal());
        orden.setFechaCreacion(new Date());
        orden.setEstado(OrdenCompra.ESTADO_PENDIENTE);
        orden.setGeneradaAutomaticamente(true);

        OrdenCompraDetalle detalle = new OrdenCompraDetalle();
        detalle.setProducto(stock.getProducto());
        detalle.setCantidad(cantidadReponer);
        orden.agregarDetalle(detalle);

        return ordenCompraRepository.save(orden);
    }

    @Override
    @Transactional
    public OrdenCompra actualizarEstado(Long id, String estado) {
        OrdenCompra orden = ordenCompraRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Orden de compra no encontrada"));
        orden.setEstado(estado);
        return ordenCompraRepository.save(orden);
    }

    @Override
    @Transactional
    public OrdenCompra marcarRecibida(Long id) {
        OrdenCompra orden = ordenCompraRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Orden de compra no encontrada"));
        if (OrdenCompra.ESTADO_RECIBIDA.equals(orden.getEstado())) {
            return orden;
        }
        for (OrdenCompraDetalle detalle : orden.getDetalles()) {
            Long sucursalId = orden.getSucursal().getId();
            Long productoId = detalle.getProducto().getId();
            stockSucursalRepository.findBySucursalIdAndProductoId(sucursalId, productoId)
                    .ifPresentOrElse(
                            existing -> stockSucursalService.ajustarCantidad(
                                    sucursalId, productoId, detalle.getCantidad()),
                            () -> {
                                StockSucursal nuevo = new StockSucursal();
                                nuevo.setSucursal(orden.getSucursal());
                                nuevo.setProducto(detalle.getProducto());
                                nuevo.setCantidad(detalle.getCantidad());
                                nuevo.setStockMinimo(10);
                                nuevo.setProveedorPreferido(orden.getProveedor());
                                stockSucursalService.save(nuevo);
                            }
                    );
        }
        orden.setEstado(OrdenCompra.ESTADO_RECIBIDA);
        return ordenCompraRepository.save(orden);
    }
}
