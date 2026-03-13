package com.inndata20.tienda.service.implementacion;

import com.inndata20.tienda.entity.DetallePedidoEntity;
import com.inndata20.tienda.entity.PedidoEntity;
import com.inndata20.tienda.entity.ProductoEntity;
import com.inndata20.tienda.model.DetallePedidoRequest;
import com.inndata20.tienda.model.DetallePedidoResponse;
import com.inndata20.tienda.model.MensajeStrResponse;
import com.inndata20.tienda.repository.DetallePedidoRepository;
import com.inndata20.tienda.repository.PedidoRepository;
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
class DetallePedidoServiceTest {

    @Mock
    DetallePedidoRepository detallePedidoRepository;

    @Mock
    PedidoRepository pedidoRepository;

    @Mock
    ProductoRepository productoRepository;

    DetallePedidoEntity detalle1;
    DetallePedidoEntity detalle2;
    DetallePedidoEntity detalle3;
    PedidoEntity pedido1;
    ProductoEntity producto1;
    ProductoEntity producto2;

    @InjectMocks
    DetallePedidoService detallePedidoService;

    @BeforeEach
    void setUp() {
        pedido1 = new PedidoEntity(1, null, 0.0, true, null);
        producto1 = new ProductoEntity(1, "Producto1", "Descripción1", 100.0, "Categoría1", 10, null, true);
        producto2 = new ProductoEntity(2, "Producto2", "Descripción2", 200.0, "Categoría2", 5, null, true);

        detalle1 = new DetallePedidoEntity(1, pedido1, producto1, 5, 100.0, true);
        detalle2 = new DetallePedidoEntity(2, pedido1, producto2, 3, 200.0, true);
        detalle3 = new DetallePedidoEntity(3, pedido1, producto1, 2, 100.0, false);
    }

    @Test
    void readAll() {
        List<DetallePedidoEntity> data = Arrays.asList(detalle1, detalle2);
        Mockito.when(detallePedidoRepository.findByActivoTrue()).thenReturn(data);
        Mockito.when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedido1));
        Mockito.when(productoRepository.findById(1)).thenReturn(Optional.of(producto1));
        Mockito.when(productoRepository.findById(2)).thenReturn(Optional.of(producto2));

        List<DetallePedidoResponse> salida = detallePedidoService.readAll();
        assertEquals(2, salida.size());
    }

    @Test
    void readById() {
        Mockito.when(detallePedidoRepository.findById(1)).thenReturn(Optional.of(detalle1));
        Mockito.when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedido1));
        Mockito.when(productoRepository.findById(1)).thenReturn(Optional.of(producto1));

        Optional<DetallePedidoResponse> salida = detallePedidoService.readById(1);
        assertTrue(salida.isPresent());
    }

    @Test
    void create() {
        Mockito.when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedido1));
        Mockito.when(productoRepository.findById(1)).thenReturn(Optional.of(producto1));
        Mockito.when(detallePedidoRepository.save(Mockito.any(DetallePedidoEntity.class))).thenReturn(detalle1);

        DetallePedidoRequest detalleNuevo = new DetallePedidoRequest();
        detalleNuevo.setPedido(1);
        detalleNuevo.setProducto(1);
        detalleNuevo.setCantidad(5);
        detalleNuevo.setPrecioUnitario(100.0);

        MensajeStrResponse salida = detallePedidoService.create(detalleNuevo);
        assertEquals("Detalle registrado exitosamente", salida.getMensaje());
    }

    @Test
    void updateById() {
        Mockito.when(detallePedidoRepository.findById(1)).thenReturn(Optional.of(detalle1));
        Mockito.when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedido1));
        Mockito.when(productoRepository.findById(1)).thenReturn(Optional.of(producto1));
        Mockito.when(detallePedidoRepository.save(Mockito.any(DetallePedidoEntity.class))).thenReturn(detalle1);

        DetallePedidoRequest detalleActualizado = new DetallePedidoRequest();
        detalleActualizado.setPedido(1);
        detalleActualizado.setProducto(1);
        detalleActualizado.setCantidad(10);
        detalleActualizado.setPrecioUnitario(90.0);

        MensajeStrResponse salida = detallePedidoService.updateById(1, detalleActualizado);
        assertEquals("Detalle registrado exitosamente", salida.getMensaje());
    }

    @Test
    void deleteById() {
        Mockito.when(detallePedidoRepository.findById(1)).thenReturn(Optional.of(detalle1));
        Mockito.when(detallePedidoRepository.save(Mockito.any(DetallePedidoEntity.class))).thenReturn(detalle1);

        MensajeStrResponse salida = detallePedidoService.deleteById(1);
        assertEquals("Detalle eliminado exitosamente", salida.getMensaje());
    }
}