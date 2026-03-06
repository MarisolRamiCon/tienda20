package com.inndata20.tienda.service;

import com.inndata20.tienda.entity.PedidoEntity;
import com.inndata20.tienda.model.PedidoDtoRequest;

import java.util.List;


public interface IPedidoService {

    List<PedidoDtoRequest> listarPedidos();

    PedidoDtoRequest buscarPorId(Integer id);

    PedidoEntity guardarPedido(PedidoDtoRequest dto);

    PedidoEntity actualizarPedido(Integer id, PedidoDtoRequest dto);

    boolean eliminarPedido(Integer id);
}
