package com.inndata20.tienda.service;

import com.inndata20.tienda.entity.PedidoEntity;

import java.util.List;


public interface IPedidoService {
    public List<PedidoEntity> readAll();

    PedidoEntity buscarPorId(Integer id);

    PedidoEntity guardarPedido(PedidoEntity pedido);

    PedidoEntity actualizarPedido(Integer id, PedidoEntity pedido);
}
