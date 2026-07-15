package com.minimarket.service.impl;

import com.minimarket.dto.DisponibilidadProductoDto;
import com.minimarket.entity.Producto;
import com.minimarket.entity.StockSucursal;
import com.minimarket.entity.Sucursal;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.repository.StockSucursalRepository;
import com.minimarket.repository.SucursalRepository;
import com.minimarket.service.OrdenCompraService;
import com.minimarket.service.StockSucursalService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StockSucursalServiceImpl implements StockSucursalService {

    private final StockSucursalRepository stockSucursalRepository;
    private final SucursalRepository sucursalRepository;
    private final ProductoRepository productoRepository;
    private final OrdenCompraService ordenCompraService;

    public StockSucursalServiceImpl(
            StockSucursalRepository stockSucursalRepository,
            SucursalRepository sucursalRepository,
            ProductoRepository productoRepository,
            @Lazy OrdenCompraService ordenCompraService
    ) {
        this.stockSucursalRepository = stockSucursalRepository;
        this.sucursalRepository = sucursalRepository;
        this.productoRepository = productoRepository;
        this.ordenCompraService = ordenCompraService;
    }

    @Override
    public List<StockSucursal> findAll() {
        return stockSucursalRepository.findAll();
    }

    @Override
    public StockSucursal findById(Long id) {
        return stockSucursalRepository.findById(id).orElse(null);
    }

    @Override
    public List<StockSucursal> findBySucursalId(Long sucursalId) {
        return stockSucursalRepository.findBySucursalId(sucursalId);
    }

    @Override
    public List<DisponibilidadProductoDto> consultarDisponibilidad(Long productoId) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        return stockSucursalRepository.findByProductoId(productoId).stream()
                .map(stock -> toDto(producto, stock))
                .toList();
    }

    @Override
    public DisponibilidadProductoDto consultarDisponibilidadEnSucursal(Long productoId, Long sucursalId) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        StockSucursal stock = stockSucursalRepository.findBySucursalIdAndProductoId(sucursalId, productoId)
                .orElse(null);
        if (stock == null) {
            Sucursal sucursal = sucursalRepository.findById(sucursalId)
                    .orElseThrow(() -> new IllegalArgumentException("Sucursal no encontrada"));
            return new DisponibilidadProductoDto(
                    producto.getId(), producto.getNombre(),
                    sucursal.getId(), sucursal.getNombre(),
                    0, false
            );
        }
        return toDto(producto, stock);
    }

    @Override
    @Transactional
    public StockSucursal save(StockSucursal stock) {
        Sucursal sucursal = sucursalRepository.findById(stock.getSucursal().getId())
                .orElseThrow(() -> new IllegalArgumentException("Sucursal no encontrada"));
        Producto producto = productoRepository.findById(stock.getProducto().getId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        stock.setSucursal(sucursal);
        stock.setProducto(producto);
        stock.validar();

        StockSucursal saved = stockSucursalRepository.findBySucursalIdAndProductoId(sucursal.getId(), producto.getId())
                .map(existente -> {
                    existente.setCantidad(stock.getCantidad());
                    existente.setStockMinimo(stock.getStockMinimo());
                    existente.setProveedorPreferido(stock.getProveedorPreferido());
                    return stockSucursalRepository.save(existente);
                })
                .orElseGet(() -> stockSucursalRepository.save(stock));

        sincronizarStockGlobal(producto.getId());
        ordenCompraService.generarAutomaticaSiCorresponde(saved);
        return saved;
    }

    @Override
    @Transactional
    public StockSucursal ajustarCantidad(Long sucursalId, Long productoId, int delta) {
        StockSucursal stock = stockSucursalRepository.findBySucursalIdAndProductoId(sucursalId, productoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe stock registrado para el producto en la sucursal"));
        int nuevaCantidad = stock.getCantidad() + delta;
        if (nuevaCantidad < 0) {
            throw new IllegalArgumentException("Stock insuficiente en la sucursal");
        }
        stock.setCantidad(nuevaCantidad);
        StockSucursal saved = stockSucursalRepository.save(stock);
        sincronizarStockGlobal(productoId);
        ordenCompraService.generarAutomaticaSiCorresponde(saved);
        return saved;
    }

    @Override
    public void deleteById(Long id) {
        stockSucursalRepository.deleteById(id);
    }

    private void sincronizarStockGlobal(Long productoId) {
        int total = stockSucursalRepository.findByProductoId(productoId).stream()
                .mapToInt(StockSucursal::getCantidad)
                .sum();
        productoRepository.findById(productoId).ifPresent(producto -> {
            producto.setStock(total);
            productoRepository.save(producto);
        });
    }

    private DisponibilidadProductoDto toDto(Producto producto, StockSucursal stock) {
        return new DisponibilidadProductoDto(
                producto.getId(),
                producto.getNombre(),
                stock.getSucursal().getId(),
                stock.getSucursal().getNombre(),
                stock.getCantidad(),
                stock.getCantidad() > 0
        );
    }
}
