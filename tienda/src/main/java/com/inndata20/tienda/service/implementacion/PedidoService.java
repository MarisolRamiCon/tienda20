package com.inndata20.tienda.service.implementacion;

import com.inndata20.tienda.entity.PedidoEntity;
import com.inndata20.tienda.repository.PedidoRepository;
import com.inndata20.tienda.service.IPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class PedidoService implements IPedidoService {
    @Autowired
    PedidoRepository pedidoRepository;


    @Override
    public List<PedidoEntity> readAll() {

        return pedidoRepository.findAll();
    }
    @Override
    public PedidoEntity buscarPorId(Integer id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    @Override
    public PedidoEntity guardarPedido(PedidoEntity pedido) {
        return pedidoRepository.save(pedido);
    }

    @Override
    public PedidoEntity actualizarPedido(Integer id, PedidoEntity pedido) {
        PedidoEntity pedidoExistente = pedidoRepository.findById(id).orElse(null);
        if (pedidoExistente != null) {
            pedidoExistente.setFechaPedido(pedido.getFechaPedido());
            pedidoExistente.setClienteId(pedido.getClienteId());
            pedidoExistente.setTotal(pedido.getTotal());
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
