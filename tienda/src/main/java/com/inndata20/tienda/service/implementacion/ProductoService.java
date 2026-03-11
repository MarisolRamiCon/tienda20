package com.inndata20.tienda.service.implementacion;

import com.inndata20.tienda.entity.ProductoEntity;
import com.inndata20.tienda.entity.ProveedoresEntity;
import com.inndata20.tienda.model.ProductoDtoRequest;
import com.inndata20.tienda.model.ProductoDtoResponse;
import com.inndata20.tienda.repository.ProductoRepository;
import com.inndata20.tienda.repository.ProveedoresRepository;
import com.inndata20.tienda.service.IProductoService;
import lombok.extern.slf4j.Slf4j; // ✅ Importamos Slf4j
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j // ✅ Agregamos la anotación para usar log
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
        log.info("Service: Consultando todos los productos activos en la base de datos");
        return productoRepository.findAll()
                .stream()
                .filter(ProductoEntity::getActivo)
                .map(this::mapearADto) // ✅ Usamos el método auxiliar
                .toList();
    }

    @Override
    public ProductoDtoResponse buscarPorId(Integer id) {
        log.info("Service: Buscando producto por ID: {}", id);
        ProductoDtoResponse response = productoRepository.findById(id)
                .filter(ProductoEntity::getActivo)
                .map(this::mapearADto)
                .orElse(null);

        if (response == null) {
            log.warn("Service: Producto con ID {} no encontrado o está inactivo", id);
        } else {
            log.info("Service: Producto '{}' encontrado con éxito", response.getNombre());
        }
        return response;
    }

    @Transactional
    @Override
    public String guardarProducto(ProductoDtoRequest dto) {
        log.info("Service: Iniciando proceso para guardar nuevo producto: {}", dto.getNombre());
        try {
            ProveedoresEntity proveedor = proveedoresRepository.findById(dto.getProveedor()).orElse(null);
            if (proveedor == null) {
                log.warn("Service: Error al guardar. No se encontró el proveedor con ID: {}", dto.getProveedor());
                return "Proveedor no encontrado";
            }

            ProductoEntity producto = new ProductoEntity();
            producto.setNombre(dto.getNombre());
            producto.setDescripcion(dto.getDescripcion());
            producto.setPrecio(dto.getPrecio());
            producto.setCategoria(dto.getCategoria());
            producto.setStock(dto.getStock());
            producto.setActivo(true);
            producto.setProveedor(proveedor);

            productoRepository.save(producto);
            log.info("Service: Producto '{}' guardado exitosamente en la BD", dto.getNombre());
            return "Producto creado exitosamente";

        } catch (DataAccessException e) {
            log.error("Service: Error de BD al intentar guardar el producto {}", dto.getNombre(), e);
            throw new ProductoServiceException("Error de acceso a la base de datos al guardar producto", e);
        } catch (Exception e) {
            log.error("Service: Error inesperado al intentar guardar el producto {}", dto.getNombre(), e);
            throw new ProductoServiceException("Error inesperado al guardar producto", e);
        }
    }

    @Transactional
    @Override
    public String actualizarProducto(Integer id, ProductoDtoRequest dto) {
        log.info("Service: Iniciando proceso de actualización para el producto ID: {}", id);
        try {
            ProductoEntity productoExistente = productoRepository.findById(id).orElse(null);
            if (productoExistente == null) {
                log.warn("Service: No se puede actualizar. Producto con ID {} no encontrado", id);
                return "Producto no encontrado";
            }

            ProveedoresEntity proveedor = proveedoresRepository.findById(dto.getProveedor()).orElse(null);
            if (proveedor == null) {
                log.warn("Service: No se puede actualizar. Proveedor con ID {} no encontrado", dto.getProveedor());
                return "Proveedor no encontrado";
            }

            productoExistente.setNombre(dto.getNombre());
            productoExistente.setDescripcion(dto.getDescripcion());
            productoExistente.setPrecio(dto.getPrecio());
            productoExistente.setCategoria(dto.getCategoria());
            productoExistente.setStock(dto.getStock());
            productoExistente.setProveedor(proveedor);

            productoRepository.save(productoExistente);
            log.info("Service: Producto con ID {} actualizado exitosamente", id);
            return "Producto actualizado exitosamente";

        } catch (DataAccessException e) {
            log.error("Service: Error de BD al intentar actualizar el producto ID {}", id, e);
            throw new ProductoServiceException("Error de acceso a la base de datos", e);
        } catch (Exception e) {
            log.error("Service: Error inesperado al actualizar el producto ID {}", id, e);
            throw new ProductoServiceException("Error inesperado al actualizar producto con id " + id, e);
        }
    }

    @Transactional
    @Override
    public boolean eliminarProducto(Integer id) {
        log.info("Service: Solicitud para eliminar lógicamente el producto con ID: {}", id);
        try {
            if (productoRepository.existsById(id)) {
                productoRepository.eliminarProducto(id);
                log.info("Service: Producto con ID {} eliminado lógicamente de la BD", id);
                return true;
            }
            log.warn("Service: No se pudo eliminar. Producto con ID {} no existe", id);
            return false;

        } catch (DataAccessException e) {
            log.error("Service: Error de BD al eliminar el producto ID {}", id, e);
            throw new ProductoServiceException("Error de acceso a la base de datos al eliminar producto con id " + id, e);
        } catch (Exception e) {
            log.error("Service: Error inesperado al eliminar el producto ID {}", id, e);
            throw new ProductoServiceException("Error inesperado al eliminar producto con id " + id, e);
        }
    }

    @Override
    public List<ProductoDtoResponse> buscarPorCategoriaYPrecio(String categoria, Double precio) {
        log.info("Service: Consultando BD por categoría '{}' y precio menor a {}", categoria, precio);
        return productoRepository.findByCategoriaAndPrecioLessThan(categoria, precio)
                .stream()
                .filter(ProductoEntity::getActivo)
                .map(this::mapearADto)
                .toList();
    }

    @Override
    public List<ProductoDtoResponse> buscarPorStockEntre(Integer stockMin, Integer stockMax) {
        log.info("Service: Consultando BD por stock entre {} y {}", stockMin, stockMax);
        return productoRepository.findByStockBetween(stockMin, stockMax)
                .stream()
                .filter(ProductoEntity::getActivo)
                .map(this::mapearADto)
                .toList();
    }

    // --- MÉTODO AUXILIAR PARA NO REPETIR CÓDIGO ---
    private ProductoDtoResponse mapearADto(ProductoEntity producto) {
        ProductoDtoResponse dto = new ProductoDtoResponse();
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setCategoria(producto.getCategoria());
        dto.setProveedor(producto.getProveedor().getId());
        dto.setStock(producto.getStock());
        return dto;
    }

    // Clase interna para manejo de excepciones
    public class ProductoServiceException extends RuntimeException {
        public ProductoServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}