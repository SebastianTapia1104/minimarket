package com.minimarket.service;

import com.minimarket.entity.Promocion;

import java.util.List;

public interface PromocionService {
    List<Promocion> findAll();

    List<Promocion> findVigentes();

    Promocion findById(Long id);

    Promocion save(Promocion promocion);

    void deleteById(Long id);
}
