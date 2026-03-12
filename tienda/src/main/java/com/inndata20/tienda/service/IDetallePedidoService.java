package com.inndata20.tienda.service;

import com.inndata20.tienda.model.DetallePedidoRequest;
import com.inndata20.tienda.model.DetallePedidoResponse;
import com.inndata20.tienda.model.MensajeStrResponse;

import java.util.List;
import java.util.Optional;

public interface IDetallePedidoService {
    List<DetallePedidoResponse> readAll();
    Optional<DetallePedidoResponse> readById(int id);

    MensajeStrResponse create(DetallePedidoRequest detallePedido);

    MensajeStrResponse updateById(int id, DetallePedidoRequest detallePedido);

    MensajeStrResponse deleteById(int id);
}
