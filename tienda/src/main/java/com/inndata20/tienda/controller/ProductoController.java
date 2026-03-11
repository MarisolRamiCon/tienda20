package com.inndata20.tienda.controller;

import com.inndata20.tienda.entity.ProductoEntity;
import com.inndata20.tienda.model.ProductoDtoRequest;
import com.inndata20.tienda.repository.ProductoRepository;
import com.inndata20.tienda.service.implementacion.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.inndata20.tienda.model.ProductoDtoResponse; // ✅ agrega esto

import java.util.List;

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
        return productoService.listarProductos();
    }

    @GetMapping("/buscar/{id}")
    public ProductoDtoResponse buscarPorId(@PathVariable Integer id) {
        return productoService.buscarPorId(id);
    }

    @PostMapping("/guardar")
    public String guardarProducto(@RequestBody ProductoDtoRequest dto) {
        return productoService.guardarProducto(dto);
    }

    @PutMapping("/actualizar/{id}")
    public String actualizarProducto(@PathVariable Integer id, @RequestBody ProductoDtoRequest dto) {
        return productoService.actualizarProducto(id, dto);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarProducto(@PathVariable Integer id) {
        if (productoService.eliminarProducto(id)) {
            return ResponseEntity.ok("Producto eliminado correctamente");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
    }

    // Filtrar por categoría y precio menor a X
    @GetMapping("/buscar/categoria")
    public ResponseEntity<List<ProductoDtoResponse>> buscarPorCategoriaYPrecio(
            @RequestParam String categoria,
            @RequestParam Double precio) {
        return ResponseEntity.ok(productoService.buscarPorCategoriaYPrecio(categoria, precio));
    }

    // Filtrar por stock entre X y Y
    @GetMapping("/buscar/stock")
    public ResponseEntity<List<ProductoDtoResponse>> buscarPorStockEntre(
            @RequestParam Integer stockMin,
            @RequestParam Integer stockMax) {
        return ResponseEntity.ok(productoService.buscarPorStockEntre(stockMin, stockMax));
    }

}