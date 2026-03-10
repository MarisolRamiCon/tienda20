package com.inndata20.tienda.service.implementacion;

import com.inndata20.tienda.entity.ProductoEntity;
import com.inndata20.tienda.entity.ProveedoresEntity;
import com.inndata20.tienda.model.ProductoDtoRequest;
import com.inndata20.tienda.model.ProductoDtoResponse;
import com.inndata20.tienda.repository.ProductoRepository;
import com.inndata20.tienda.repository.ProveedoresRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)

class ProductoServiceTest {

    @Mock
    ProductoRepository productoRepository;
    @Mock
    ProveedoresRepository proveedoresRepository;
    @InjectMocks
    ProductoService productoService;

    @Test
    void listarProductos() {
        // Arrange
        when(productoRepository.findAll()).thenReturn(List.of());

        // Act
        List<ProductoDtoResponse> resultado = productoService.listarProductos();

        // Assert
        assertNotNull(resultado);
    }

    @Test
    void buscarPorId() {
        // Arrange
        ProveedoresEntity proveedor = new ProveedoresEntity();
        proveedor.setId(1);

        ProductoEntity producto = new ProductoEntity();
        producto.setId(1);
        producto.setNombre("Laptop HP");
        producto.setProveedor(proveedor);

        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));

        // Act
        ProductoDtoResponse resultado = productoService.buscarPorId(1);

// Assert
        assertNotNull(resultado);
        assertEquals("Laptop HP", resultado.getNombre());
    }

    @Test
    void guardarProducto() {
        // Arrange
        ProductoDtoRequest dto = new ProductoDtoRequest();
        dto.setNombre("Laptop HP");
        dto.setProveedor(1);

        ProveedoresEntity proveedor = new ProveedoresEntity();
        proveedor.setId(1);

        ProductoEntity producto = new ProductoEntity();
        producto.setNombre("Laptop HP");
        producto.setProveedor(proveedor);

        when(proveedoresRepository.findById(1)).thenReturn(Optional.of(proveedor));
        when(productoRepository.save(any(ProductoEntity.class))).thenReturn(producto);

        // Act
        String resultado = productoService.guardarProducto(dto);

// Assert
        assertNotNull(resultado);
        assertEquals("Producto guardado correctamente", resultado);
    }

    @Test
    void actualizarProducto() {
        // Arrange
        ProveedoresEntity proveedor = new ProveedoresEntity();
        proveedor.setId(1);

        ProductoEntity productoExistente = new ProductoEntity();
        productoExistente.setId(1);
        productoExistente.setNombre("Laptop HP");
        productoExistente.setProveedor(proveedor);

        ProductoDtoRequest dto = new ProductoDtoRequest();
        dto.setNombre("Laptop Lenovo");
        dto.setProveedor(1);

        when(productoRepository.findById(1)).thenReturn(Optional.of(productoExistente));
        when(proveedoresRepository.findById(1)).thenReturn(Optional.of(proveedor));
        when(productoRepository.save(productoExistente)).thenReturn(productoExistente);

        // Act
        String resultado = productoService.actualizarProducto(1, dto);

// Assert
        assertNotNull(resultado);
        assertEquals("Producto actualizado correctamente", resultado);
    }

    @Test
    void eliminarProducto() {
        // Arrange
        when(productoRepository.existsById(1)).thenReturn(true);
        when(productoRepository.existsById(99)).thenReturn(false);

        // Act y Assert
        assertTrue(productoService.eliminarProducto(1));
        assertFalse(productoService.eliminarProducto(99));
    }
}