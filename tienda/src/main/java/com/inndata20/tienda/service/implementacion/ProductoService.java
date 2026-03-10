package com.inndata20.tienda.service.implementacion;

import com.inndata20.tienda.entity.ProductoEntity;
import com.inndata20.tienda.entity.ProveedoresEntity;
import com.inndata20.tienda.model.ProductoDtoRequest;
import com.inndata20.tienda.repository.ProductoRepository;
import com.inndata20.tienda.repository.ProveedoresRepository;
import com.inndata20.tienda.service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import com.inndata20.tienda.model.ProductoDtoResponse; // ✅ agrega esto

import java.util.List;

@Service
public class ProductoService implements IProductoService {

    private final ProductoRepository productoRepository;
    private final ProveedoresRepository proveedoresRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository,
                           ProveedoresRepository proveedoresRepository) {
        this.productoRepository = productoRepository;
        this.proveedoresRepository = proveedoresRepository;
    }


    @Override
    public List<ProductoDtoResponse> listarProductos() {
        return productoRepository.findAll()
                .stream()
                .filter(ProductoEntity::getActivo) // ✅ solo activos
                .map(producto -> {
                    ProductoDtoResponse dto = new ProductoDtoResponse();
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

    @Override
    public ProductoDtoResponse buscarPorId(Integer id) {
        return productoRepository.findById(id)
                .filter(ProductoEntity::getActivo) // ✅ solo si está activo
                .map(producto -> {
                    ProductoDtoResponse dto = new ProductoDtoResponse();
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

    @Override
    public String guardarProducto(ProductoDtoRequest dto) { // ✅ retorna String

        try {

            ProveedoresEntity proveedor = proveedoresRepository.findById(dto.getProveedor()).orElse(null);
            if (proveedor == null) return "Proveedor no encontrado";

            ProductoEntity producto = new ProductoEntity();
            producto.setNombre(dto.getNombre());
            producto.setDescripcion(dto.getDescripcion());
            producto.setPrecio(dto.getPrecio());
            producto.setCategoria(dto.getCategoria());
            producto.setStock(dto.getStock());
            producto.setActivo(true); // ✅ siempre inicia activo
            producto.setProveedor(proveedor);
            productoRepository.save(producto);
            return "Producto creado exitosamente";
        } catch (DataAccessException e) {
            // Manejo de excepciones específicas de acceso a datos
            throw new ProductoServiceException("Error de acceso a la base de datos al guardar producto", e);

        } catch (Exception e) {
            // Manejo de cualquier otra excepción
            throw new ProductoServiceException("Error inesperado al guardar producto", e);

        }
    }

    @Override
    public String actualizarProducto(Integer id, ProductoDtoRequest dto) { // ✅ retorna String

        try {


            ProductoEntity productoExistente = productoRepository.findById(id).orElse(null);
            if (productoExistente == null) return "Producto no encontrado";

            ProveedoresEntity proveedor = proveedoresRepository.findById(dto.getProveedor()).orElse(null);
            if (proveedor == null) return "Proveedor no encontrado";

            productoExistente.setNombre(dto.getNombre());
            productoExistente.setDescripcion(dto.getDescripcion());
            productoExistente.setPrecio(dto.getPrecio());
            productoExistente.setCategoria(dto.getCategoria());
            productoExistente.setStock(dto.getStock());
            productoExistente.setProveedor(proveedor);
            productoRepository.save(productoExistente);
            return "Producto actualizado exitosamente";
        } catch (DataAccessException e){

            throw new ProductoServiceException("Error de acceso a la base de datos", e);

        } catch (Exception e){

            throw new ProductoServiceException("Error inesperado al actualizar producto con id " + id, e);

        }


    }

    // Esto es para manejo de excepciones específicas del servicio de productos
    public class ProductoServiceException extends RuntimeException {
        public ProductoServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    @Override
    public boolean eliminarProducto(Integer id) {

        try {

            if (productoRepository.existsById(id)) {
                productoRepository.eliminarProducto(id);
                return true;
            }
            return false;
        } catch (DataAccessException e) {
            // Manejo de excepciones específicas de acceso a datos
            throw new ProductoServiceException("Error de acceso a la base de datos al eliminar producto con id " + id, e);

        } catch (Exception e) {
            // Manejo de cualquier otra excepción
            throw new ProductoServiceException("Error inesperado al eliminar producto con id " + id, e);

        }
    }
}