package com.inndata20.tienda.service.implementacion;

import com.inndata20.tienda.entity.ClienteEntity;
import com.inndata20.tienda.entity.PedidoEntity;
import com.inndata20.tienda.model.PedidoDtoRequest;
import com.inndata20.tienda.model.PedidoDtoResponse;
import com.inndata20.tienda.repository.ClienteRepository;
import com.inndata20.tienda.repository.PedidoRepository;
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


class PedidoServiceTest {

    @Mock
    PedidoRepository pedidoRepository;
    @Mock
    ClienteRepository clienteRepository;
    @InjectMocks
    PedidoService pedidoService;

    @Test
    void listarPedidos() {
        // Arrange
        when(pedidoRepository.findAll()).thenReturn(List.of());

        // Act
        List<PedidoDtoResponse> resultado = pedidoService.listarPedidos();

        // Assert
        assertNotNull(resultado);
    }

    @Test
    void buscarPorId() {
        // Arrange
        PedidoEntity pedido = new PedidoEntity();
        pedido.setId(1);
        pedido.setCliente(new ClienteEntity());

        when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedido));

        // Act
        PedidoDtoResponse resultado = pedidoService.buscarPorId(1);

        // Assert
        assertNotNull(resultado);
    }

    @Test
    void guardarPedido() {
        // Arrange
        PedidoDtoRequest dto = new PedidoDtoRequest();
        dto.setTotal(100.0);
        dto.setClienteId(1);

        ClienteEntity cliente = new ClienteEntity();
        cliente.setId(1);

        PedidoEntity pedido = new PedidoEntity();
        pedido.setTotal(100.0);
        pedido.setCliente(cliente);

        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(pedidoRepository.save(any(PedidoEntity.class))).thenReturn(pedido);

        // Act

        String resultado = pedidoService.guardarPedido(dto);


        // Assert
        assertNotNull(resultado);
        assertEquals("Pedido actualizado correctamente", resultado);

    }

    @Test
    void actualizarPedido() {
        // Arrange
        ClienteEntity cliente = new ClienteEntity();
        cliente.setId(1);

        PedidoEntity pedidoExistente = new PedidoEntity();
        pedidoExistente.setId(1);
        pedidoExistente.setTotal(100.0);
        pedidoExistente.setCliente(cliente);

        PedidoDtoRequest dto = new PedidoDtoRequest();
        dto.setTotal(200.0);
        dto.setClienteId(1);

        when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedidoExistente));
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(pedidoRepository.save(pedidoExistente)).thenReturn(pedidoExistente);

        // Act
        String resultado = pedidoService.actualizarPedido(1, dto);

        // Assert
        assertNotNull(resultado);
        assertEquals("Pedido actualizado correctamente", resultado);
    }

    @Test
    void eliminarPedido() {
        // Arrange
        when(pedidoRepository.existsById(1)).thenReturn(true);
        when(pedidoRepository.existsById(99)).thenReturn(false);

        // Act y Assert
        assertTrue(pedidoService.eliminarPedido(1));
        assertFalse(pedidoService.eliminarPedido(99));
    }
}