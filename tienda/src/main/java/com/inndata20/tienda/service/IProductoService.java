package com.inndata20.tienda.service;

import com.inndata20.tienda.entity.ProductoEntity;
import com.inndata20.tienda.model.ProductoDtoRequest;

import java.util.List;

public interface IProductoService {

    List<ProductoDtoRequest> listarProductos();
    ProductoDtoRequest buscarPorId(Integer id);
    ProductoEntity guardarProducto(ProductoDtoRequest dto);
    ProductoEntity actualizarProducto(Integer id, ProductoDtoRequest dto);
    boolean eliminarProducto(Integer id);



    /*List<ProductoEntity> listarProductos();
    ProductoEntity buscarPorId(Integer id);
    ProductoEntity guardarProducto(ProductoEntity producto);
    ProductoEntity actualizarProducto(Integer id, ProductoEntity producto);
    boolean eliminarProducto(Integer id);
*/

}