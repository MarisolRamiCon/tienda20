package com.inndata20.tienda.service;

import com.inndata20.tienda.entity.ProductoEntity;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IProductoService {

    List<ProductoEntity> listarProductos();
    ProductosEntity buscarPorId(Integer id);
    ProductosEntity guardarEmpleado(ProductosEntity productos);
    ProductosEntity actualizarEmpleado(Integer id, ProductosEntity productos);

}
