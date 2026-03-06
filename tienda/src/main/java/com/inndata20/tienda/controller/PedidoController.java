package com.inndata20.tienda.controller;

import com.inndata20.tienda.entity.PedidoEntity;
import com.inndata20.tienda.service.implementacion.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
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
    public PedidoEntity guardarPedido(@RequestBody PedidoEntity pedido) {
        return pedidoService.guardarPedido(pedido);
    }

    // ACTUALIZA UN PEDIDO EXISTENTE

    @PutMapping("/actualizar/{id}")
    public PedidoEntity actualizarPedido(@PathVariable Integer id, @RequestBody PedidoEntity pedido) {
        return pedidoService.actualizarPedido(id, pedido);
    }


    // ELIMINAR UN PEDIDO EXISTENTE

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarPedido(@PathVariable Integer id) {
        if (pedidoService.eliminarPedido(id)) {
            return ResponseEntity.ok("Producto eliminado correctamente");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
    }


}
