package com.minimarket.service;

import com.minimarket.entity.OrdenCompra;
import com.minimarket.entity.StockSucursal;

import java.util.List;

public interface OrdenCompraService {
    List<OrdenCompra> findAll();

    OrdenCompra findById(Long id);

    OrdenCompra crearManual(OrdenCompra orden);

    OrdenCompra generarAutomaticaSiCorresponde(StockSucursal stock);

    OrdenCompra actualizarEstado(Long id, String estado);

    OrdenCompra marcarRecibida(Long id);
}
