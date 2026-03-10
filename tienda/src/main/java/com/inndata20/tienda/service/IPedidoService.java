package com.inndata20.tienda.service;

import com.inndata20.tienda.model.PedidoDtoRequest;
import com.inndata20.tienda.model.PedidoDtoResponse;

import java.util.List;

public interface IPedidoService {

    List<PedidoDtoResponse> listarPedidos();
    PedidoDtoResponse buscarPorId(Integer id);
    String guardarPedido(PedidoDtoRequest dto);
    String actualizarPedido(Integer id, PedidoDtoRequest dto);
    boolean eliminarPedido(Integer id);
}