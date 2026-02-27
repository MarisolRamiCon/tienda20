package com.inndata20.tienda.service;

import com.inndata20.tienda.entity.ClienteEntity;

import java.util.List;

public interface IClienteService {
    public List<ClienteEntity> readAll();
}
