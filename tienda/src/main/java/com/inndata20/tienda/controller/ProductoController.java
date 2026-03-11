package com.inndata20.tienda.controller;
import com.inndata20.tienda.model.ProductoDtoRequest;
import com.inndata20.tienda.service.implementacion.ProductoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.inndata20.tienda.model.ProductoDtoResponse; // ✅ agrega esto

import java.util.List;

@Slf4j // Agrega esta anotación para habilitar el logging con Lombok
@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private final ProductoService productoService;

    @Autowired
    public ProductoController(ProductoService productoService) {
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
    public String guardarProducto(@RequestBody ProductoDtoRequest dto) {
        log.info("REST Request: Petición para guardar un nuevo producto");
        return productoService.guardarProducto(dto);
    }

    @PutMapping("/actualizar/{id}")
    public String actualizarProducto(@PathVariable Integer id, @RequestBody ProductoDtoRequest dto) {
        log.info("REST Request: Petición para actualizar el producto con ID: {}", id);
        return productoService.actualizarProducto(id, dto);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarProducto(@PathVariable Integer id) {
        log.info("REST Request: Petición para eliminar lógicamente el producto con ID: {}", id);
        if (productoService.eliminarProducto(id)) {
            log.info("Producto con ID: {} eliminado correctamente", id);
            return ResponseEntity.ok("Producto eliminado correctamente");
        }
        log.warn("REST Request Fallido: No se pudo eliminar, producto con ID: {} no encontrado", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
    }

    // Filtrar por categoría y precio menor a X
    @GetMapping("/buscar/categoria")
    public ResponseEntity<List<ProductoDtoResponse>> buscarPorCategoriaYPrecio(
            @RequestParam String categoria,
            @RequestParam Double precio) {
        log.info("REST Request: Buscando productos de la categoría '{}' con precio menor a {}", categoria, precio);
        return ResponseEntity.ok(productoService.buscarPorCategoriaYPrecio(categoria, precio));
    }

    // Filtrar por stock entre X y Y
    @GetMapping("/buscar/stock")
    public ResponseEntity<List<ProductoDtoResponse>> buscarPorStockEntre(
            @RequestParam Integer stockMin,
            @RequestParam Integer stockMax) {
        log.info("REST Request: Buscando productos con stock entre {} y {}", stockMin, stockMax);
        return ResponseEntity.ok(productoService.buscarPorStockEntre(stockMin, stockMax));
    }

}