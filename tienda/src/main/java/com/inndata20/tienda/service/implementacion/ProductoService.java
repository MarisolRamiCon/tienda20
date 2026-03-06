package com.inndata20.tienda.service.implementacion;

import com.inndata20.tienda.entity.ProductoEntity;
import com.inndata20.tienda.entity.ProveedoresEntity;
import com.inndata20.tienda.model.ProductoDtoRequest;
import com.inndata20.tienda.repository.ProductoRepository;
import com.inndata20.tienda.repository.ProveedoresRepository;
import com.inndata20.tienda.service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService implements IProductoService {

    @Autowired
    ProductoRepository productoRepository;

    @Override
    public List<ProductoDtoRequest> listarProductos() {
        return productoRepository.findAll()
                .stream()
                .map(producto -> {
                    ProductoDtoRequest dto = new ProductoDtoRequest();
                    dto.setNombre(producto.getNombre());
                    dto.setDescripcion(producto.getDescripcion());
                    dto.setPrecio(producto.getPrecio());
                    dto.setCategoria(producto.getCategoria());
                    dto.setProveedor(producto.getProveedor().getId());
                    dto.setStock(producto.getStock());
                    return dto;
                })
                .toList();
    }

    /*@Override
    public List<ProductoDtoRequest> listarProductos() {
        return productoRepository.findAll();
    }*/

    @Override
    public ProductoDtoRequest buscarPorId(Integer id) {
        return productoRepository.findById(id)
                .map(producto -> {
                    ProductoDtoRequest dto = new ProductoDtoRequest();
                    dto.setNombre(producto.getNombre());
                    dto.setDescripcion(producto.getDescripcion());
                    dto.setPrecio(producto.getPrecio());
                    dto.setCategoria(producto.getCategoria());
                    dto.setProveedor(producto.getProveedor().getId());
                    dto.setStock(producto.getStock());
                    return dto;
                })
                .orElse(null);
    }


    /*@Override
    public ProductoDtoRequest buscarPorId(Integer id){
        return productoRepository.findById(id).orElse(null);
    }*/
    @Autowired
    ProveedoresRepository proveedoresRepository;
    @Override
    public ProductoEntity guardarProducto(ProductoDtoRequest dto) {
        ProductoEntity producto = new ProductoEntity();
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setCategoria(dto.getCategoria());
        producto.setStock(dto.getStock());
        ProveedoresEntity proveedor = proveedoresRepository.findById(dto.getProveedor()).orElse(null);
        producto.setProveedor(proveedor);
        return productoRepository.save(producto);
    }

    /*@Override
    public ProductoEntity guardarProducto(ProductoEntity producto){
        return productoRepository.save(producto);
    }*/
    @Autowired
    ProveedoresRepository proveedoresRepository;
    @Override
    public ProductoEntity actualizarProducto(Integer id, ProductoDtoRequest dto) {
        ProductoEntity productoExistente = productoRepository.findById(id).orElse(null);
        if (productoExistente != null) {
            productoExistente.setNombre(dto.getNombre());
            productoExistente.setDescripcion(dto.getDescripcion());
            productoExistente.setPrecio(dto.getPrecio());
            productoExistente.setCategoria(dto.getCategoria());
            productoExistente.setStock(dto.getStock());
            ProveedoresEntity proveedor = proveedoresRepository.findById(dto.getProveedor()).orElse(null);
            productoExistente.setProveedor(proveedor);
            return productoRepository.save(productoExistente);
        }
        return null;
    }

    /*@Override
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
    }*/

    @Override
    public boolean eliminarProducto(Integer id) {
        if (productoRepository.existsById(id)) {
            productoRepository.eliminarProducto(id);
            return true;
        }
        return false;
    }



    /*@Override
    public boolean eliminarProducto(Integer id) {
        if (productoRepository.existsById(id)) {
            productoRepository.eliminarProducto(id);
            return true;
        }
        return false;
    }*/



}