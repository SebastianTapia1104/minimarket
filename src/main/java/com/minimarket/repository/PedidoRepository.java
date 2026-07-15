package com.minimarket.repository;

import com.minimarket.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByClienteId(Long clienteId);

    List<Pedido> findBySucursalId(Long sucursalId);

    List<Pedido> findByEstado(String estado);
}
