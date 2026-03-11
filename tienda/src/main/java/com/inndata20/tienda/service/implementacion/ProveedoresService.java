package com.inndata20.tienda.service.implementacion;


import com.inndata20.tienda.entity.ProveedoresEntity;
import com.inndata20.tienda.repository.ProveedoresRepository;
import com.inndata20.tienda.service.IProveedoresService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class ProveedoresService implements IProveedoresService {

    private final ProveedoresRepository proveedoresRepository;

    public ProveedoresService(ProveedoresRepository proveedoresRepository) {
        this.proveedoresRepository = proveedoresRepository;
    }

    @Override
    public List<ProveedoresEntity> readAll() {
        return proveedoresRepository.findAll();
    }
}
