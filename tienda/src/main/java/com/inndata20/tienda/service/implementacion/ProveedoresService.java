package com.inndata20.tienda.service.implementacion;


import com.inndata20.tienda.entity.ProveedoresEntity;
import com.inndata20.tienda.repository.ProveedoresRepository;
import com.inndata20.tienda.service.IProveedoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class ProveedoresService implements IProveedoresService {
    @Autowired
    ProveedoresRepository proveedoresRepository;

    @Override
    public List<ProveedoresEntity> readAll() {
        return proveedoresRepository.findAll();
    }
}
