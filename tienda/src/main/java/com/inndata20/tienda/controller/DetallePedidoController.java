package com.inndata20.tienda.controller;

import com.inndata20.tienda.entity.DetallePedidoEntity;
import com.inndata20.tienda.service.implementacion.DetallePedidoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")

public class DetallePedidoController {

    private final DetallePedidoService detallePedidoService;

    public DetallePedidoController(DetallePedidoService detallePedidoService) {
        this.detallePedidoService = detallePedidoService;
    }

    @GetMapping("/detallepedido/{id}")
    public Optional<DetallePedidoEntity> readById(@PathVariable int id) {
        return detallePedidoService.readById(id);
    }
}
