package com.inndata20.tienda.service;

import com.inndata20.tienda.entity.PedidoEntity;

import java.util.List;


public interface IPedidoService {
    public List<PedidoEntity> readAll();

    PedidoEntity buscarPorId(Integer id);

    PedidoEntity guardarPersona(PedidoEntity pedido);

    PedidoEntity actualizarPersona(Integer id, PedidoEntity pedido);


}
