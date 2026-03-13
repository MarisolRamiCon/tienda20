package com.inndata20.tienda.service;

import com.inndata20.tienda.model.MensajeDtoResponse;
import com.inndata20.tienda.model.ProductoDtoRequest;
import com.inndata20.tienda.model.ProductoDtoResponse;

import java.util.List;

public interface IProductoService {

    List<ProductoDtoResponse> listarProductos();
    ProductoDtoResponse buscarPorId(Integer id);

    MensajeDtoResponse guardarProducto(ProductoDtoRequest productoRequest);
    MensajeDtoResponse actualizarProducto(Integer id, ProductoDtoRequest productoRequest);
    MensajeDtoResponse eliminarProducto(Integer id);

    // Filtrar por categoría y precio menor a X
    List<ProductoDtoResponse> buscarPorCategoriaYPrecio(String categoria, Double precio);

    // Filtrar por stock entre X y Y
    List<ProductoDtoResponse> buscarPorStockEntre(Integer stockMin, Integer stockMax);

    // QUERYS PERSONALIZADOS
    List<ProductoDtoResponse> buscarPorNombreYCategoria(String nombre, String categoria);
    List<ProductoDtoResponse> buscarPorProveedor(Integer proveedorId);

}