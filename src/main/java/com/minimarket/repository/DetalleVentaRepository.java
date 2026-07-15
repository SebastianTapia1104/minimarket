package com.minimarket.repository;

import com.minimarket.entity.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
    List<DetalleVenta> findByVentaId(Long ventaId);

    @Query("""
            SELECT d.producto.id, d.producto.nombre, SUM(d.cantidad)
            FROM DetalleVenta d
            GROUP BY d.producto.id, d.producto.nombre
            ORDER BY SUM(d.cantidad) DESC
            """)
    List<Object[]> findRotacionProductos();
}
