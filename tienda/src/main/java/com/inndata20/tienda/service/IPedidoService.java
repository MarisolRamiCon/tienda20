package com.inndata20.tienda.service;

import com.inndata20.tienda.model.MensajeDtoResponse;
import com.inndata20.tienda.model.PedidoDtoRequest;
import com.inndata20.tienda.model.PedidoDtoResponse;

import java.time.LocalDate;
import java.util.List;

public interface IPedidoService {

    List<PedidoDtoResponse> listarPedidos();
    PedidoDtoResponse buscarPorId(Integer id);
    MensajeDtoResponse guardarPedido(PedidoDtoRequest dto);
    MensajeDtoResponse actualizarPedido(Integer id, PedidoDtoRequest dto);
    boolean eliminarPedido(Integer id);

    // METODOS JPA PERSONALIZADOS
    List<PedidoDtoResponse> buscarPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin);
    List<PedidoDtoResponse> buscarPorCliente(Integer clienteId);

    // QUERYS PERSONALIZADOS

    List<PedidoDtoResponse> buscarPorRangoTotal(Double min, Double max);
    List<PedidoDtoResponse> buscarPedidosActivosPorCliente(Integer clienteId);
}