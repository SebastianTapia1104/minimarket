package com.minimarket.service.impl;

import com.minimarket.entity.Sucursal;
import com.minimarket.repository.SucursalRepository;
import com.minimarket.service.SucursalService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SucursalServiceImpl implements SucursalService {

    private final SucursalRepository sucursalRepository;

    public SucursalServiceImpl(SucursalRepository sucursalRepository) {
        this.sucursalRepository = sucursalRepository;
    }

    @Override
    public List<Sucursal> findAll() {
        return sucursalRepository.findAll();
    }

    @Override
    public Sucursal findById(Long id) {
        return sucursalRepository.findById(id).orElse(null);
    }

    @Override
    public Sucursal save(Sucursal sucursal) {
        if (sucursal.getNombre() == null || sucursal.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la sucursal es obligatorio");
        }
        return sucursalRepository.save(sucursal);
    }

    @Override
    public void deleteById(Long id) {
        sucursalRepository.deleteById(id);
    }
}
