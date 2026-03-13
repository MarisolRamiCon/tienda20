package com.inndata20.tienda.controller;

import com.inndata20.tienda.model.MensajeDtoResponse;
import com.inndata20.tienda.model.ProductoDtoRequest;
import com.inndata20.tienda.model.ProductoDtoResponse;
import com.inndata20.tienda.service.IProductoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private final IProductoService productoService;

    @Autowired
    public ProductoController(IProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/listar")
    public List<ProductoDtoResponse> listarProductos() {
        log.info("REST Request: Solicitando la lista de todos los productos");
        return productoService.listarProductos();
    }

    @GetMapping("/buscar/{id}")
    public ProductoDtoResponse buscarPorId(@PathVariable Integer id) {
        log.info("REST Request: Buscando producto con ID: {}", id);
        return productoService.buscarPorId(id);
    }

    @PostMapping("/guardar")
    public MensajeDtoResponse guardarProducto(@RequestBody ProductoDtoRequest productoRequest) {
        log.info("REST Request: Petición para guardar un nuevo producto: {}", productoRequest.getNombre());
        return productoService.guardarProducto(productoRequest);
    }

    @PutMapping("/actualizar/{id}")
    public MensajeDtoResponse actualizarProducto(@PathVariable Integer id, @RequestBody ProductoDtoRequest productoRequest) {
        log.info("REST Request: Petición para actualizar el producto con ID: {}", id);
        return productoService.actualizarProducto(id, productoRequest);
    }

    @DeleteMapping("/eliminar/{id}")
    public MensajeDtoResponse eliminarProducto(@PathVariable Integer id) {
        log.info("REST Request: Petición para eliminar lógicamente el producto con ID: {}", id);
        return productoService.eliminarProducto(id);
    }

    // METODOS JPA PERSONALIZADOS

    @GetMapping("/buscar/categoria")
    public ResponseEntity<List<ProductoDtoResponse>> buscarPorCategoriaYPrecio(
            @RequestParam String categoria,
            @RequestParam Double precio) {
        log.info("REST Request: Buscando productos de la categoría '{}' con precio menor a {}", categoria, precio);
        return ResponseEntity.ok(productoService.buscarPorCategoriaYPrecio(categoria, precio));
    }

    @GetMapping("/buscar/stock")
    public ResponseEntity<List<ProductoDtoResponse>> buscarPorStockEntre(
            @RequestParam Integer stockMin,
            @RequestParam Integer stockMax) {
        log.info("REST Request: Buscando productos con stock entre {} y {}", stockMin, stockMax);
        return ResponseEntity.ok(productoService.buscarPorStockEntre(stockMin, stockMax));
    }

    // QUERYS PERSONALIZADOS

    @GetMapping("/buscar/nombre-categoria")
    public ResponseEntity<List<ProductoDtoResponse>> buscarPorNombreYCategoria(
            @RequestParam String nombre,
            @RequestParam String categoria) {
        log.info("REST Request: Buscando productos con nombre '{}' y categoría '{}'", nombre, categoria);
        return ResponseEntity.ok(productoService.buscarPorNombreYCategoria(nombre, categoria));
    }

    @GetMapping("/buscar/proveedor/{proveedorId}")
    public ResponseEntity<List<ProductoDtoResponse>> buscarPorProveedor(
            @PathVariable Integer proveedorId) {
        log.info("REST Request: Buscando productos del proveedor con ID: {}", proveedorId);
        return ResponseEntity.ok(productoService.buscarPorProveedor(proveedorId));
    }
}