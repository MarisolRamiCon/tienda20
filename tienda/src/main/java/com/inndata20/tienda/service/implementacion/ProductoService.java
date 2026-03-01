package com.inndata20.tienda.service.implementacion;

import com.inndata20.tienda.entity.ProductoEntity;
import com.inndata20.tienda.repository.ProductoRepository;
import com.inndata20.tienda.service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService implements IProductoService {

    @Autowired
    ProductoRepository productoRepository;

    @Override
    public List<ProductoEntity> listarProductos() {
        return productoRepository.findAll();
    }

    @Override
    public ProductoEntity buscarPorId(Integer id){
        return productoRepository.findById(id).orElse(null);
    }

    @Override
    public ProductoEntity guardarProducto(ProductoEntity producto){
        return productoRepository.save(producto);
    }

    @Override
    public ProductoEntity actualizarProducto(Integer id, ProductoEntity producto) {
        ProductoEntity productoExistente = productoRepository.findById(id).orElse(null);
        if (productoExistente != null) {
            productoExistente.setNombre(producto.getNombre());
            productoExistente.setDescripcion(producto.getDescripcion());
            productoExistente.setPrecio(producto.getPrecio());
            productoExistente.setCategoria(producto.getCategoria());
            productoExistente.setProveedor(producto.getProveedor());
            productoExistente.setStock(producto.getStock());
            return productoRepository.save(productoExistente);
        }
        return null;
    }

}