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
    public MensajeDtoResponse eliminarProducto(@PathVariable Integer id) {
        log.info("REST Request: Petición para eliminar lógicamente el producto con ID: {}", id);
        if (productoService.eliminarProducto(id)) {
            log.info("Producto con ID: {} eliminado correctamente", id);
            return new MensajeDtoResponse("Producto eliminado correctamente",true);
        }
        log.warn("REST Request Fallido: No se pudo eliminar, producto con ID: {} no encontrado", id);
        return new MensajeDtoResponse("Producto no encontrado o ya eliminado", false);
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

    // GET /api/v1/productos/buscar/nombre-categoria?nombre=camisa&categoria=ropa
    //GET /api/v1/productos/buscar/proveedor/1
}