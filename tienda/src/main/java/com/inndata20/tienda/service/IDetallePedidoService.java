package com.inndata20.tienda.service;

import com.inndata20.tienda.entity.DetallePedidoEntity;

import java.util.List;
import java.util.Optional;

public interface IDetallePedidoService {
    List<DetallePedidoEntity> readAllByClienteId(int id);
    Optional<DetallePedidoEntity> readById(int id);
}
