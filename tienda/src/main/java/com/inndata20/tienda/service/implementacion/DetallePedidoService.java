package com.inndata20.tienda.service.implementacion;

import com.inndata20.tienda.entity.DetallePedidoEntity;

import com.inndata20.tienda.repository.DetallePedidoRepository;
import com.inndata20.tienda.service.IDetallePedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class DetallePedidoService implements IDetallePedidoService {

    private final DetallePedidoRepository detallePedidoRepository;

    public DetallePedidoService(DetallePedidoRepository detallePedidoRepository) {
        this.detallePedidoRepository = detallePedidoRepository;
    }

    @Override
    public List<DetallePedidoEntity> readAllByClienteId(int id) {
        return detallePedidoRepository.findAll();
    }

    @Override
    public Optional<DetallePedidoEntity> readById(int id) {
        return detallePedidoRepository.findById(id);
    }
}
