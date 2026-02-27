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
}
