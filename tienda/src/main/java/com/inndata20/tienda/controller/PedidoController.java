package com.inndata20.tienda.controller;

import com.inndata20.tienda.entity.PedidoEntity;
import com.inndata20.tienda.service.implementacion.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    @Autowired
    PedidoService pedidoService;

    @GetMapping("/listar")
    public List<PedidoEntity> readAll(){
        return pedidoService.readAll();
    }

    // BUSCA POR ID

    @GetMapping("/buscar/{id}")
    public PedidoEntity buscarPorId(@PathVariable Integer id) {
        return pedidoService.buscarPorId(id);
    }

    // GUARDA UN PEDIDO

    @PostMapping("/guardar")
    public PedidoEntity guardarPersona(@RequestBody PedidoEntity persona) {
        return pedidoService.guardarPersona(persona);
    }

    // ACTUALIZA UN PEDIDO EXISTENTE

    @PutMapping("/actualizar/{id}")
    public PedidoEntity actualizarPersona(@PathVariable Integer id, @RequestBody PedidoEntity persona) {
        return pedidoService.actualizarPedido(id, persona);
    }


}
