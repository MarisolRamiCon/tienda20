package com.inndata20.tienda.service;

import com.inndata20.tienda.model.ClienteDtoRequest;
import com.inndata20.tienda.model.ClienteDtoResponse;

import java.util.List;
import java.util.Optional;

public interface IClienteService {
    public List<ClienteDtoResponse> readAll();
    Optional<ClienteDtoResponse> readById(int id);

    String create(ClienteDtoRequest cliente);
}
