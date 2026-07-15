package com.minimarket.service.impl;

import com.minimarket.entity.Promocion;
import com.minimarket.repository.PromocionRepository;
import com.minimarket.service.PromocionService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PromocionServiceImpl implements PromocionService {

    private final PromocionRepository promocionRepository;

    public PromocionServiceImpl(PromocionRepository promocionRepository) {
        this.promocionRepository = promocionRepository;
    }

    @Override
    public List<Promocion> findAll() {
        return promocionRepository.findAll();
    }

    @Override
    public List<Promocion> findVigentes() {
        return promocionRepository.findVigentes(new Date());
    }

    @Override
    public Promocion findById(Long id) {
        return promocionRepository.findById(id).orElse(null);
    }

    @Override
    public Promocion save(Promocion promocion) {
        promocion.validar();
        return promocionRepository.save(promocion);
    }

    @Override
    public void deleteById(Long id) {
        promocionRepository.deleteById(id);
    }
}
