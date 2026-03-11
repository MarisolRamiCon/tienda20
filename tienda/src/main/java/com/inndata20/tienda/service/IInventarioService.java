package com.inndata20.tienda.service;

import com.inndata20.tienda.entity.InventarioEntity;

import java.util.List;

public interface IInventarioService {
    public List<InventarioEntity> readAll();
    public InventarioEntity readById(int id);
}
