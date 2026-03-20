package com.inndata20.tienda.service;

import com.inndata20.tienda.entity.InventarioEntity;
import com.inndata20.tienda.model.InventarioRequest;
import com.inndata20.tienda.model.InventarioResponse;
import com.inndata20.tienda.model.MensajeStrResponse;

import java.util.List;

public interface IInventarioService {
    public List<InventarioResponse> readAll();
    public InventarioResponse readById(int id);
    public MensajeStrResponse create(InventarioRequest inventarioRequest);
    public MensajeStrResponse updateById(int id, InventarioRequest inventarioRequest);
    public MensajeStrResponse deleteById(int id);
}
