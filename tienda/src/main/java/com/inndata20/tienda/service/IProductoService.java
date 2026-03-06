package com.inndata20.tienda.service;

import com.inndata20.tienda.entity.ProductoEntity;
import java.util.List;

public interface IProductoService {

    List<ProductoEntity> listarProductos();
    ProductoEntity buscarPorId(Integer id);
    ProductoEntity guardarProducto(ProductoEntity producto);
    ProductoEntity actualizarProducto(Integer id, ProductoEntity producto);
    boolean eliminarProducto(Integer id);


}