package com.inndata20.tienda.service.implementacion;

import com.inndata20.tienda.entity.InventarioEntity;
import com.inndata20.tienda.repository.InventarioRepository;
import com.inndata20.tienda.service.IInventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class InventarioService implements IInventarioService {
    @Autowired
    InventarioRepository inventarioRepository;

    @Override
    public List<InventarioEntity> readAll() {
        return List.of();
    }

    @Override
    public InventarioEntity readById(int id) {
        return null;
    }
}
