package com.inndata20.tienda.controller;

import com.inndata20.tienda.model.MensajeDtoResponse;
import com.inndata20.tienda.model.ProductoDtoRequest;
import com.inndata20.tienda.model.ProductoDtoResponse;
import com.inndata20.tienda.service.IProductoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    // GET a /api/v1/productos
    @GetMapping
    public ResponseEntity<List<ProductoDtoResponse>> listarProductos() {
        log.info("REST Request: Solicitando la lista de todos los productos");
        return ResponseEntity.ok(productoService.listarProductos());
    }

    // GET a /api/v1/productos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDtoResponse> buscarPorId(@PathVariable Integer id) {
        log.info("REST Request: Buscando producto con ID: {}", id);
        ProductoDtoResponse producto = productoService.buscarPorId(id);

        if (producto == null) {
            return ResponseEntity.notFound().build(); // Retorna 404 si no existe
        }
        return ResponseEntity.ok(producto); // Retorna 200 OK si lo encuentra
    }

    // POST a /api/v1/productos
    @PostMapping
    public ResponseEntity<MensajeDtoResponse> guardarProducto(@RequestBody ProductoDtoRequest productoRequest) {
        log.info("REST Request: Petición para guardar un nuevo producto: {}", productoRequest.getNombre());
        MensajeDtoResponse response = productoService.guardarProducto(productoRequest);

        if (response.getExito()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response); // Retorna 201 Created
        }
        return ResponseEntity.badRequest().body(response); // Retorna 400 Bad Request
    }

    // PUT a /api/v1/productos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<MensajeDtoResponse> actualizarProducto(@PathVariable Integer id, @RequestBody ProductoDtoRequest productoRequest) {
        log.info("REST Request: Petición para actualizar el producto con ID: {}", id);
        MensajeDtoResponse response = productoService.actualizarProducto(id, productoRequest);

        if (response.getExito()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    // DELETE a /api/v1/productos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeDtoResponse> eliminarProducto(@PathVariable Integer id) {
        log.info("REST Request: Petición para eliminar lógicamente el producto con ID: {}", id);
        MensajeDtoResponse response = productoService.eliminarProducto(id);

        if (response.getExito()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    // ==========================================
    // METODOS JPA Y QUERYS PERSONALIZADOS
    // ==========================================

    @GetMapping("/categoria") // Le quitamos el "buscar"
    public ResponseEntity<List<ProductoDtoResponse>> buscarPorCategoriaYPrecio(
            @RequestParam String categoria,
            @RequestParam Double precio) {
        log.info("REST Request: Buscando productos de la categoría '{}' con precio menor a {}", categoria, precio);
        return ResponseEntity.ok(productoService.buscarPorCategoriaYPrecio(categoria, precio));
    }

    @GetMapping("/stock") // Le quitamos el "buscar"
    public ResponseEntity<List<ProductoDtoResponse>> buscarPorStockEntre(
            @RequestParam Integer stockMin,
            @RequestParam Integer stockMax) {
        log.info("REST Request: Buscando productos con stock entre {} y {}", stockMin, stockMax);
        return ResponseEntity.ok(productoService.buscarPorStockEntre(stockMin, stockMax));
    }

    @GetMapping("/busqueda") // Queda más limpio usar un endpoint genérico para filtros
    public ResponseEntity<List<ProductoDtoResponse>> buscarPorNombreYCategoria(
            @RequestParam String nombre,
            @RequestParam String categoria) {
        log.info("REST Request: Buscando productos con nombre '{}' y categoría '{}'", nombre, categoria);
        return ResponseEntity.ok(productoService.buscarPorNombreYCategoria(nombre, categoria));
    }

    @GetMapping("/proveedor/{proveedorId}") // Le quitamos el "buscar"
    public ResponseEntity<List<ProductoDtoResponse>> buscarPorProveedor(
            @PathVariable Integer proveedorId) {
        log.info("REST Request: Buscando productos del proveedor con ID: {}", proveedorId);
        return ResponseEntity.ok(productoService.buscarPorProveedor(proveedorId));
    }
}