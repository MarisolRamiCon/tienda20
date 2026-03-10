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
}