package com.inndata20.tienda.controller;

import com.inndata20.tienda.entity.ProductoEntity;
import com.inndata20.tienda.service.implementacion.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class ProductoController {
    @Autowired
    ProductoService productoService;

    @GetMapping("/listar")
    public List<ProductoEntity> listarProductos(){
        return productoService.listarProductos();
    }

    // BUSCA POR ID

    @GetMapping("/buscar/{id}")
    public ProductoEntity buscarPorId(@PathVariable Integer id) {
        return productoService.buscarPorId(id);
    }

    // GUARDA UN PEDIDO

    @PostMapping("/guardar")
    public ProductoEntity guardarProducto(@RequestBody ProductoEntity producto) {
        return productoService.guardarProducto(producto);
    }

    // ACTUALIZA UN PEDIDO EXISTENTE

    @PutMapping("/actualizar/{id}")
    public ProductoEntity actualizarProducto(@PathVariable Integer id, @RequestBody ProductoEntity producto) {
        return productoService.actualizarProducto(id, producto);
    }


}