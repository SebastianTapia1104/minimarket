package com.minimarket.service;

import com.minimarket.dto.DisponibilidadProductoDto;
import com.minimarket.entity.StockSucursal;

import java.util.List;

public interface StockSucursalService {
    List<StockSucursal> findAll();

    StockSucursal findById(Long id);

    List<StockSucursal> findBySucursalId(Long sucursalId);

    List<DisponibilidadProductoDto> consultarDisponibilidad(Long productoId);

    DisponibilidadProductoDto consultarDisponibilidadEnSucursal(Long productoId, Long sucursalId);

    StockSucursal save(StockSucursal stock);

    StockSucursal ajustarCantidad(Long sucursalId, Long productoId, int delta);

    void deleteById(Long id);
}
