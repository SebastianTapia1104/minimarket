package com.minimarket.repository;

import com.minimarket.entity.Promocion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface PromocionRepository extends JpaRepository<Promocion, Long> {
    List<Promocion> findByActivaTrue();

    @Query("""
            SELECT p FROM Promocion p
            WHERE p.activa = true
              AND p.fechaInicio <= :fecha
              AND p.fechaFin >= :fecha
            """)
    List<Promocion> findVigentes(Date fecha);
}
