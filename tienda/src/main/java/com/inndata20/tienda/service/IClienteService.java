package com.inndata20.tienda.service;

import com.inndata20.tienda.model.ClienteDtoRequest;
import com.inndata20.tienda.model.ClienteDtoResponse;
import com.inndata20.tienda.model.MensajeStrResponse;

import java.util.List;
import java.util.Optional;

public interface IClienteService {
    public List<ClienteDtoResponse> readAll();
    Optional<ClienteDtoResponse> readById(int id);

    MensajeStrResponse create(ClienteDtoRequest cliente);

    MensajeStrResponse updateById(int id, ClienteDtoRequest entrada);

    MensajeStrResponse deleteById(int id);
}
