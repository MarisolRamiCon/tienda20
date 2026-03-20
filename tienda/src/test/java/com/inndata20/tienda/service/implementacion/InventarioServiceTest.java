package com.inndata20.tienda.service.implementacion;

import com.inndata20.tienda.entity.InventarioEntity;
import com.inndata20.tienda.entity.ProductoEntity;
import com.inndata20.tienda.model.InventarioRequest;
import com.inndata20.tienda.model.InventarioResponse;
import com.inndata20.tienda.model.MensajeStrResponse;
import com.inndata20.tienda.repository.InventarioRepository;
import com.inndata20.tienda.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {

    @Mock
    InventarioRepository inventarioRepository;

    @Mock
    ProductoRepository productoRepository;

    InventarioEntity inventario1;
    InventarioEntity inventario2;
    InventarioEntity inventario3;
    ProductoEntity producto1;
    ProductoEntity producto2;

    @InjectMocks
    InventarioService inventarioService;

    @BeforeEach
    void setUp() {
        producto1 = new ProductoEntity(1, "Laptop", "Laptop de 15 pulgadas", 1500.0, "Electrónica", 10, null, true);
        producto2 = new ProductoEntity(2, "Mouse", "Mouse inalámbrico", 25.0, "Periféricos", 50, null, true);

        inventario1 = new InventarioEntity(1, producto1, 50, true);
        inventario2 = new InventarioEntity(2, producto2, 200, true);
        inventario3 = new InventarioEntity(3, producto1, 0, false);
    }

    @Test
    void readAll() {
        List<InventarioEntity> data = Arrays.asList(inventario1, inventario2);
        Mockito.when(inventarioRepository.findByActivoTrue()).thenReturn(data);
        Mockito.when(productoRepository.findById(1)).thenReturn(Optional.of(producto1));
        Mockito.when(productoRepository.findById(2)).thenReturn(Optional.of(producto2));

        List<InventarioResponse> salida = inventarioService.readAll();
        assertEquals(2, salida.size());
    }

    @Test
    void readById() {
        Mockito.when(inventarioRepository.findById(1)).thenReturn(Optional.of(inventario1));
        Mockito.when(productoRepository.findById(1)).thenReturn(Optional.of(producto1));

        InventarioResponse salida = inventarioService.readById(1);
        assertNotNull(salida);
        assertEquals(50, salida.getCantidadStock());
    }

    @Test
    void create() {
        Mockito.when(productoRepository.findById(1)).thenReturn(Optional.of(producto1));
        Mockito.when(inventarioRepository.save(Mockito.any(InventarioEntity.class))).thenReturn(inventario1);

        InventarioRequest inventarioNuevo = new InventarioRequest();
        inventarioNuevo.setProducto(1);
        inventarioNuevo.setCantidadStock(50);

        MensajeStrResponse salida = inventarioService.create(inventarioNuevo);
        assertEquals("Inventario creado exitosamente", salida.getMensaje());
    }

    @Test
    void updateById() {
        Mockito.when(inventarioRepository.findById(1)).thenReturn(Optional.of(inventario1));
        Mockito.when(productoRepository.findById(1)).thenReturn(Optional.of(producto1));
        Mockito.when(inventarioRepository.save(Mockito.any(InventarioEntity.class))).thenReturn(inventario1);

        InventarioRequest inventarioActualizado = new InventarioRequest();
        inventarioActualizado.setProducto(1);
        inventarioActualizado.setCantidadStock(75);

        MensajeStrResponse salida = inventarioService.updateById(1, inventarioActualizado);
        assertEquals("Inventario actualizado exitosamente", salida.getMensaje());
    }

    @Test
    void deleteById() {
        Mockito.when(inventarioRepository.findById(1)).thenReturn(Optional.of(inventario1));
        Mockito.when(inventarioRepository.save(Mockito.any(InventarioEntity.class))).thenReturn(inventario1);

        MensajeStrResponse salida = inventarioService.deleteById(1);
        assertEquals("Inventario eliminado exitosamente", salida.getMensaje());
    }
}