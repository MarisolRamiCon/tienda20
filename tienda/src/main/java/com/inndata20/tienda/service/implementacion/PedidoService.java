package com.inndata20.tienda.service.implementacion;

import com.inndata20.tienda.entity.ClienteEntity;
import com.inndata20.tienda.entity.PedidoEntity;
import com.inndata20.tienda.entity.ProductoEntity;
import com.inndata20.tienda.entity.ProveedoresEntity;
import com.inndata20.tienda.model.PedidoDtoRequest;
import com.inndata20.tienda.model.ProductoDtoRequest;
import com.inndata20.tienda.repository.ClienteRepository;
import com.inndata20.tienda.repository.PedidoRepository;
import com.inndata20.tienda.service.IPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class PedidoService implements IPedidoService {
    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Override
    public List<PedidoDtoRequest> listarPedidos() {
        return pedidoRepository.findAll()
                .stream()
                .map(pedido -> {
                    PedidoDtoRequest dto = new PedidoDtoRequest();
                    dto.setFechaPedido(pedido.getFechaPedido());
                    dto.setTotal(pedido.getTotal());
                    dto.setActivo(pedido.getActivo());
                    dto.setClienteId(pedido.getCliente().getId());
                    return dto;
                })
                .toList();
    }


    @Override
    public PedidoDtoRequest buscarPorId(Integer id) {
        return pedidoRepository.findById(id)
                .map(pedido -> {
                    PedidoDtoRequest dto = new PedidoDtoRequest();
                    dto.setFechaPedido(pedido.getFechaPedido());
                    dto.setTotal(pedido.getTotal());
                    dto.setActivo(pedido.getActivo());
                    dto.setClienteId(pedido.getCliente().getId());
                    return dto;
                })
                .orElse(null);
    }

    @Override
    public PedidoEntity guardarPedido(PedidoDtoRequest dto) {
        PedidoEntity pedido = new PedidoEntity();
        pedido.setFechaPedido(dto.getFechaPedido());
        pedido.setTotal(dto.getTotal());
        pedido.setActivo(dto.getActivo());
        ClienteEntity cliente = clienteRepository.findById(dto.getClienteId()).orElse(null);
        pedido.setCliente(cliente);
        return pedidoRepository.save(pedido);
    }

    @Override
    public PedidoEntity actualizarPedido(Integer id, PedidoDtoRequest dto) {
        PedidoEntity pedidoExistente = pedidoRepository.findById(id).orElse(null);
        if (pedidoExistente != null) {
            pedidoExistente.setFechaPedido(dto.getFechaPedido());
            pedidoExistente.setTotal(dto.getTotal());
            pedidoExistente.setActivo(dto.getActivo());
            ClienteEntity cliente = clienteRepository.findById(dto.getClienteId()).orElse(null);
            pedidoExistente.setCliente(cliente);
            return pedidoRepository.save(pedidoExistente);
        }
        return null;
    }

    @Override
    public boolean eliminarPedido(Integer id) {
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.eliminarPedido(id);
            return true;
        }
        return false;
    }


}




