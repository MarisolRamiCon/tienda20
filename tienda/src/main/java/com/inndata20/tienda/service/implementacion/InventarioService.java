package com.inndata20.tienda.service.implementacion;

import com.inndata20.tienda.entity.InventarioEntity;
import com.inndata20.tienda.repository.InventarioRepository;
import com.inndata20.tienda.service.IInventarioService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class InventarioService implements IInventarioService {

    private final InventarioRepository inventarioRepository;

    public InventarioService(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    @Override
    public List<InventarioEntity> readAll() {
        return inventarioRepository.findAll();
    }

    @Override
    public InventarioEntity readById(int id) {
        return inventarioRepository.findById(id).orElse(null);
    }
}
