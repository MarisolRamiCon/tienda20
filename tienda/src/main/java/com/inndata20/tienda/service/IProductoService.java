package com.inndata20.tienda.service;

import com.inndata20.tienda.model.ProductoDtoRequest;
import com.inndata20.tienda.model.ProductoDtoResponse;

import java.util.List;

public interface IProductoService {

    List<ProductoDtoResponse> listarProductos();
    ProductoDtoResponse buscarPorId(Integer id);
    String guardarProducto(ProductoDtoRequest dto);
    String actualizarProducto(Integer id, ProductoDtoRequest dto);
    boolean eliminarProducto(Integer id);

    //  Filtrar por categoría y precio menor a X
    List<ProductoDtoResponse> buscarPorCategoriaYPrecio(String categoria, Double precio);

    // Filtrar por stock entre X y Y
    List<ProductoDtoResponse> buscarPorStockEntre(Integer stockMin, Integer stockMax);

    // QUERYS PERSONALIZADOS
    List<ProductoDtoResponse> buscarPorNombreYCategoria(String nombre, String categoria);
    List<ProductoDtoResponse> buscarPorProveedor(Integer proveedorId);

}