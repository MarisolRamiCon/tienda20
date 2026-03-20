package com.inndata20.tienda.service;

import com.inndata20.tienda.model.MensajeStrResponse;
import com.inndata20.tienda.model.ProveedoresRequest;
import com.inndata20.tienda.model.ProveedoresResponse;

import java.util.List;

public interface IProveedoresService {
    public List<ProveedoresResponse> readAll();
    public ProveedoresResponse readById(int id);
    MensajeStrResponse create(ProveedoresRequest entrada);
    public MensajeStrResponse updateById(int id, ProveedoresRequest proveedoresRequest);
    public MensajeStrResponse deleteById(int id);
}
