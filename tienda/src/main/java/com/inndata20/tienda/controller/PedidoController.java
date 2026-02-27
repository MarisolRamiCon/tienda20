package com.inndata20.tienda.controller;

import com.inndata20.tienda.entity.PedidoEntity;
import com.inndata20.tienda.service.implementacion.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PedidoController {
    @Autowired
    PedidoService pedidoService;

    @GetMapping("/pedidos")
    public List<PedidoEntity> readAll(){
        return pedidoService.readAll();
    }
}
