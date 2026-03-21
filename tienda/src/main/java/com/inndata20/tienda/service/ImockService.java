package com.inndata20.tienda.service;

import com.inndata20.tienda.feign.APIMock;


import java.util.List;

public interface ImockService {


    List<APIMock> listarProductos();

    //  Buscar por ID
    APIMock obtenerProducto(String id);

    //  Crear
    APIMock crearProducto(APIMock producto);

    //  Actualizar
    APIMock actualizarProducto(String id, APIMock producto);

    //  Eliminar
    void eliminarProducto(String id);


}
