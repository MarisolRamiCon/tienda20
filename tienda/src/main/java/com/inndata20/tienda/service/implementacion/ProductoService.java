package com.inndata20.tienda.service.implementacion;

import com.inndata20.tienda.entity.ProductoEntity;
import com.inndata20.tienda.entity.ProveedoresEntity;
import com.inndata20.tienda.model.MensajeDtoResponse;
import com.inndata20.tienda.model.ProductoDtoRequest;
import com.inndata20.tienda.model.ProductoDtoResponse;
import com.inndata20.tienda.repository.ProductoRepository;
import com.inndata20.tienda.repository.ProveedoresRepository;
import com.inndata20.tienda.service.IProductoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
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
        try {
            return productoRepository.findAll()
                    .stream()
                    .filter(ProductoEntity::getActivo) // Recuerda: Sería ideal pasarlo a findAllByActivoTrue() en el Repository
                    .map(this::mapearADto)
                    .toList();
        } catch (DataAccessException e) {
            log.error("Service: Error de BD al consultar productos", e);
            return List.of(); // Cambiamos throw por lista vacía
        } catch (Exception e) {
            log.error("Service: Error inesperado al consultar productos", e);
            return List.of(); // Cambiamos throw por lista vacía
        }
    }

    @Override
    public ProductoDtoResponse buscarPorId(Integer id) {
        log.info("Service: Buscando producto por ID: {}", id);
        try {
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
        } catch (DataAccessException e) {
            log.error("Service: Error de BD al buscar producto con ID {}", id, e);
            return null; // Cambiamos throw por null
        } catch (Exception e) {
            log.error("Service: Error inesperado al buscar producto con ID {}", id, e);
            return null; // Cambiamos throw por null
        }
    }

    @Transactional
    @Override
    public MensajeDtoResponse guardarProducto(ProductoDtoRequest productoRequest) {
        log.info("Service: Iniciando proceso para guardar nuevo producto: {}", productoRequest.getNombre());
        try {
            ProveedoresEntity proveedor = proveedoresRepository.findById(productoRequest.getProveedor()).orElse(null);
            if (proveedor == null) {
                log.warn("Service: Error al guardar. No se encontró el proveedor con ID: {}", productoRequest.getProveedor());
                return new MensajeDtoResponse("Proveedor no encontrado", false);
            }

            ProductoEntity producto = new ProductoEntity();
            producto.setNombre(productoRequest.getNombre());
            producto.setDescripcion(productoRequest.getDescripcion());
            producto.setPrecio(productoRequest.getPrecio());
            producto.setCategoria(productoRequest.getCategoria());
            producto.setStock(productoRequest.getStock());
            producto.setActivo(true);
            producto.setProveedor(proveedor);

            productoRepository.save(producto);
            log.info("Service: Producto '{}' guardado exitosamente en la BD", productoRequest.getNombre());
            return new MensajeDtoResponse("Producto guardado exitosamente", true);

        } catch (DataAccessException e) {
            log.error("Service: Error de BD al intentar guardar el producto {}", productoRequest.getNombre(), e);
            return new MensajeDtoResponse("Error de base de datos al guardar producto", false);
        } catch (Exception e) {
            log.error("Service: Error inesperado al intentar guardar el producto {}", productoRequest.getNombre(), e);
            return new MensajeDtoResponse("Error inesperado al guardar producto", false);
        }
    }

    @Transactional
    @Override
    public MensajeDtoResponse actualizarProducto(Integer id, ProductoDtoRequest productoRequest) {
        log.info("Service: Iniciando proceso de actualización para el producto ID: {}", id);
        try {
            ProductoEntity productoExistente = productoRepository.findById(id).orElse(null);
            if (productoExistente == null) {
                log.warn("Service: No se puede actualizar. Producto con ID {} no encontrado", id);
                return new MensajeDtoResponse("Producto no encontrado", false);
            }

            ProveedoresEntity proveedor = proveedoresRepository.findById(productoRequest.getProveedor()).orElse(null);
            if (proveedor == null) {
                log.warn("Service: No se puede actualizar. Proveedor con ID {} no encontrado", productoRequest.getProveedor());
                return new MensajeDtoResponse("Proveedor no encontrado", false);
            }

            productoExistente.setNombre(productoRequest.getNombre());
            productoExistente.setDescripcion(productoRequest.getDescripcion());
            productoExistente.setPrecio(productoRequest.getPrecio());
            productoExistente.setCategoria(productoRequest.getCategoria());
            productoExistente.setStock(productoRequest.getStock());
            productoExistente.setProveedor(proveedor);

            productoRepository.save(productoExistente);
            log.info("Service: Producto con ID {} actualizado exitosamente", id);
            return new MensajeDtoResponse("Producto actualizado exitosamente", true);

        } catch (DataAccessException e) {
            log.error("Service: Error de BD al intentar actualizar el producto ID {}", id, e);
            return new MensajeDtoResponse("Error de base de datos al actualizar producto", false);
        } catch (Exception e) {
            log.error("Service: Error inesperado al actualizar el producto ID {}", id, e);
            return new MensajeDtoResponse("Error inesperado al actualizar producto", false);
        }
    }

    @Transactional
    @Override
    public MensajeDtoResponse eliminarProducto(Integer id) {
        log.info("Service: Solicitud para eliminar lógicamente el producto con ID: {}", id);
        try {
            if (productoRepository.existsById(id)) {
                productoRepository.eliminarProducto(id);
                log.info("Service: Producto con ID {} eliminado lógicamente de la BD", id);
                return new MensajeDtoResponse("Producto eliminado exitosamente", true);
            }
            log.warn("Service: No se pudo eliminar. Producto con ID {} no existe", id);
            return new MensajeDtoResponse("Producto no encontrado", false);
        } catch (DataAccessException e) {
            log.error("Service: Error de BD al eliminar el producto ID {}", id, e);
            return new MensajeDtoResponse("Error de base de datos al eliminar producto", false);
        } catch (Exception e) {
            log.error("Service: Error inesperado al eliminar el producto ID {}", id, e);
            return new MensajeDtoResponse("Error inesperado al eliminar producto", false);
        }
    }

    // JPA PERSONALIZADOS

    @Override
    public List<ProductoDtoResponse> buscarPorCategoriaYPrecio(String categoria, Double precio) {
        log.info("Service: Consultando BD por categoría '{}' y precio menor a {}", categoria, precio);
        try {
            return productoRepository.findByCategoriaAndPrecioLessThan(categoria, precio)
                    .stream()
                    .filter(ProductoEntity::getActivo)
                    .map(this::mapearADto)
                    .toList();
        } catch (DataAccessException e) {
            log.error("Service: Error de BD al buscar por categoría '{}' y precio", categoria, e);
            return List.of();
        } catch (Exception e) {
            log.error("Service: Error inesperado al buscar por categoría '{}' y precio", categoria, e);
            return List.of();
        }
    }

    @Override
    public List<ProductoDtoResponse> buscarPorStockEntre(Integer stockMin, Integer stockMax) {
        log.info("Service: Consultando BD por stock entre {} y {}", stockMin, stockMax);
        try {
            return productoRepository.findByStockBetween(stockMin, stockMax)
                    .stream()
                    .filter(ProductoEntity::getActivo)
                    .map(this::mapearADto)
                    .toList();
        } catch (DataAccessException e) {
            log.error("Service: Error de BD al buscar por stock entre {} y {}", stockMin, stockMax, e);
            return List.of();
        } catch (Exception e) {
            log.error("Service: Error inesperado al buscar por stock entre {} y {}", stockMin, stockMax, e);
            return List.of();
        }
    }

    // QUERYS PERSONALIZADOS

    @Override
    public List<ProductoDtoResponse> buscarPorNombreYCategoria(String nombre, String categoria) {
        log.info("Service: Consultando BD por nombre '{}' y categoría '{}'", nombre, categoria);
        try {
            return productoRepository.buscarPorNombreYCategoria(nombre, categoria)
                    .stream()
                    .map(this::mapearADto)
                    .toList();
        } catch (DataAccessException e) {
            log.error("Service: Error de BD al buscar por nombre '{}' y categoría '{}'", nombre, categoria, e);
            return List.of();
        } catch (Exception e) {
            log.error("Service: Error inesperado al buscar por nombre '{}' y categoría '{}'", nombre, categoria, e);
            return List.of();
        }
    }

    @Override
    public List<ProductoDtoResponse> buscarPorProveedor(Integer proveedorId) {
        log.info("Service: Consultando BD por proveedor con ID: {}", proveedorId);
        try {
            return productoRepository.buscarPorProveedor(proveedorId)
                    .stream()
                    .map(this::mapearADto)
                    .toList();
        } catch (DataAccessException e) {
            log.error("Service: Error de BD al buscar por proveedor ID: {}", proveedorId, e);
            return List.of();
        } catch (Exception e) {
            log.error("Service: Error inesperado al buscar por proveedor ID: {}", proveedorId, e);
            return List.of();
        }
    }

    // --- METODO AUXILIAR PARA NO REPETIR CÓDIGO ---
    private ProductoDtoResponse mapearADto(ProductoEntity producto) {
        ProductoDtoResponse productoResponse = new ProductoDtoResponse();

        
        productoResponse.setNombre(producto.getNombre());
        productoResponse.setDescripcion(producto.getDescripcion());
        productoResponse.setPrecio(producto.getPrecio());
        productoResponse.setCategoria(producto.getCategoria());
        productoResponse.setStock(producto.getStock());

        // Evitamos un NullPointerException si por alguna razón el proveedor viene nulo
        if (producto.getProveedor() != null) {
            productoResponse.setProveedor(producto.getProveedor().getId());
        }

        return productoResponse;
    }
}