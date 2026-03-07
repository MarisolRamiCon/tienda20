package com.inndata20.tienda.service;

import com.inndata20.tienda.entity.ClienteEntity;

import java.util.List;
import java.util.Optional;

public interface IClienteService {
    public List<ClienteEntity> readAll();
    Optional<ClienteEntity> readById(int id);
}
