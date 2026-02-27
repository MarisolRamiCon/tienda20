package com.inndata20.tienda.service.implementacion;


import com.inndata20.tienda.entity.ClienteEntity;
import com.inndata20.tienda.repository.ClienteRepository;
import com.inndata20.tienda.service.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class ClienteService implements IClienteService {
    @Autowired
    ClienteRepository clienteRepository;

    @Override
    public List<ClienteEntity> readAll() {
        return clienteRepository.findAll();
    }
}
