package com.minimarket.repository;

import com.minimarket.entity.OrdenCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Long> {
    List<OrdenCompra> findBySucursalId(Long sucursalId);

    List<OrdenCompra> findByEstado(String estado);

    @Query("""
            SELECT COUNT(o) > 0 FROM OrdenCompra o
            JOIN o.detalles d
            WHERE o.sucursal.id = :sucursalId
              AND d.producto.id = :productoId
              AND o.estado IN ('PENDIENTE', 'ENVIADA')
            """)
    boolean existsPendientePorProducto(
            @Param("sucursalId") Long sucursalId,
            @Param("productoId") Long productoId
    );
}
