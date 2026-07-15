package com.minimarket.repository;

import com.minimarket.entity.StockSucursal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockSucursalRepository extends JpaRepository<StockSucursal, Long> {
    List<StockSucursal> findBySucursalId(Long sucursalId);

    List<StockSucursal> findByProductoId(Long productoId);

    Optional<StockSucursal> findBySucursalIdAndProductoId(Long sucursalId, Long productoId);

    List<StockSucursal> findByCantidadLessThanEqualAndStockMinimoIsNotNull(Integer cantidad);
}
