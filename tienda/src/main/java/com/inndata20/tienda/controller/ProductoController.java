package com.inndata20.tienda.controller;

import com.inndata20.tienda.entity.ProductoEntity;
import com.inndata20.tienda.model.ProductoDtoRequest;
import com.inndata20.tienda.service.implementacion.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {
    @Autowired
    ProductoService productoService;

    @GetMapping("/listar")
    public List<ProductoDtoRequest> listarProductos(){
        return productoService.listarProductos();
    }

    // BUSCA POR ID

    @GetMapping("/buscar/{id}")
    public ProductoDtoRequest buscarPorId(@PathVariable Integer id) {
        return productoService.buscarPorId(id);
    }

    // GUARDA UN PEDIDO

    @PostMapping("/guardar")
    public ProductoEntity guardarProducto(@RequestBody ProductoDtoRequest dto) {
        return productoService.guardarProducto(dto);
    }

    // ACTUALIZA UN PEDIDO EXISTENTE

    @PutMapping("/actualizar/{id}")
    public ProductoEntity actualizarProducto(@PathVariable Integer id, @RequestBody ProductoDtoRequest dto) {
        return productoService.actualizarProducto(id, dto);

    }

    // ELIMINAR UN PEDIDO EXISTENTE

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Integer id) {
        if (productoService.eliminarProducto(id)) {
            return ResponseEntity.ok("Producto eliminado correctamente");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
    }



}