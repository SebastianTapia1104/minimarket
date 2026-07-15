package com.minimarket.service;

import com.minimarket.entity.Pedido;

import java.util.List;

public interface PedidoService {
    List<Pedido> findAll();

    Pedido findById(Long id);

    List<Pedido> findByClienteId(Long clienteId);

    Pedido crearPedido(Pedido pedido);

    Pedido actualizarEstado(Long id, String estado);
}
