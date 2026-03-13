package com.inndata20.tienda.service.implementacion;

import com.inndata20.tienda.entity.ClienteEntity;
import com.inndata20.tienda.entity.PedidoEntity;
import com.inndata20.tienda.model.MensajeDtoResponse;
import com.inndata20.tienda.model.PedidoDtoRequest;
import com.inndata20.tienda.model.PedidoDtoResponse;
import com.inndata20.tienda.repository.ClienteRepository;
import com.inndata20.tienda.repository.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    PedidoRepository pedidoRepository;

    @Mock
    ClienteRepository clienteRepository;

    @InjectMocks
    PedidoService pedidoService;

    @Test
    void listarPedidos_filtraInactivos_y_mapeaADto() {
        PedidoEntity activo = pedidoEntity(1, LocalDate.of(2026, 1, 1), 100.0, true, clienteEntity(10));
        PedidoEntity inactivo = pedidoEntity(2, LocalDate.of(2026, 1, 2), 200.0, false, clienteEntity(10));
        when(pedidoRepository.findAll()).thenReturn(List.of(activo, inactivo));

        List<PedidoDtoResponse> resultado = pedidoService.listarPedidos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(100.0, resultado.get(0).getTotal());
        assertEquals(10, resultado.get(0).getClienteId());
    }

    @Test
    void listarPedidos_siOcurreExcepcionInesperada_devuelveListaVacia() {
        PedidoEntity activoConNpe = pedidoEntity(1, LocalDate.of(2026, 1, 1), 100.0, true, null);
        when(pedidoRepository.findAll()).thenReturn(List.of(activoConNpe));

        List<PedidoDtoResponse> resultado = pedidoService.listarPedidos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void listarPedidos_siRepositorioFalla_devuelveListaVacia() {
        when(pedidoRepository.findAll()).thenThrow(new DataAccessResourceFailureException("db down"));

        List<PedidoDtoResponse> resultado = pedidoService.listarPedidos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void buscarPorId_cuandoExisteYActivo_devuelveDto() {
        PedidoEntity pedido = pedidoEntity(1, LocalDate.of(2026, 1, 1), 100.0, true, clienteEntity(10));
        when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedido));

        PedidoDtoResponse resultado = pedidoService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(LocalDate.of(2026, 1, 1), resultado.getFechaPedido());
        assertEquals(100.0, resultado.getTotal());
        assertEquals(10, resultado.getClienteId());
    }

    @Test
    void buscarPorId_cuandoNoExiste_o_inactivo_devuelveNull() {
        when(pedidoRepository.findById(1)).thenReturn(Optional.empty());
        assertNull(pedidoService.buscarPorId(1));

        PedidoEntity inactivo = pedidoEntity(2, LocalDate.of(2026, 1, 2), 200.0, false, clienteEntity(10));
        when(pedidoRepository.findById(2)).thenReturn(Optional.of(inactivo));
        assertNull(pedidoService.buscarPorId(2));
    }

    @Test
    void buscarPorId_siRepositorioFalla_devuelveNull() {
        when(pedidoRepository.findById(1)).thenThrow(new DataAccessResourceFailureException("db down"));
        assertNull(pedidoService.buscarPorId(1));
    }

    @Test
    void guardarPedido_cuandoClienteExiste_guarda_y_devuelveExito() {
        ClienteEntity cliente = clienteEntity(10);
        when(clienteRepository.findById(10)).thenReturn(Optional.of(cliente));
        when(pedidoRepository.save(any(PedidoEntity.class))).thenAnswer(inv -> inv.getArgument(0, PedidoEntity.class));

        PedidoDtoRequest req = new PedidoDtoRequest();
        req.setFechaPedido(LocalDate.of(2026, 1, 5));
        req.setTotal(150.0);
        req.setClienteId(10);

        ArgumentCaptor<PedidoEntity> captor = ArgumentCaptor.forClass(PedidoEntity.class);
        MensajeDtoResponse resp = pedidoService.guardarPedido(req);

        assertNotNull(resp);
        assertTrue(resp.getExito());
        assertEquals("Pedido guardado exitosamente", resp.getMensaje());

        verify(pedidoRepository).save(captor.capture());
        PedidoEntity guardado = captor.getValue();
        assertEquals(LocalDate.of(2026, 1, 5), guardado.getFechaPedido());
        assertEquals(150.0, guardado.getTotal());
        assertEquals(Boolean.TRUE, guardado.getActivo());
        assertEquals(10, guardado.getCliente().getId());
    }

    @Test
    void guardarPedido_cuandoClienteNoExiste_devuelveError_y_noGuarda() {
        when(clienteRepository.findById(10)).thenReturn(Optional.empty());

        PedidoDtoRequest req = new PedidoDtoRequest();
        req.setClienteId(10);

        MensajeDtoResponse resp = pedidoService.guardarPedido(req);

        assertNotNull(resp);
        assertFalse(resp.getExito());
        assertEquals("Error: Cliente no encontrado ", resp.getMensaje());
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void guardarPedido_siRepositorioFalla_devuelveError() {
        ClienteEntity cliente = clienteEntity(10);
        when(clienteRepository.findById(10)).thenReturn(Optional.of(cliente));
        when(pedidoRepository.save(any(PedidoEntity.class))).thenThrow(new DataAccessResourceFailureException("db down"));

        PedidoDtoRequest req = new PedidoDtoRequest();
        req.setClienteId(10);

        MensajeDtoResponse resp = pedidoService.guardarPedido(req);

        assertNotNull(resp);
        assertFalse(resp.getExito());
        assertEquals("Error de acceso a la base de datos al guardar pedido", resp.getMensaje());
    }

    @Test
    void guardarPedido_siOcurreExcepcionInesperada_devuelveError() {
        ClienteEntity cliente = clienteEntity(10);
        when(clienteRepository.findById(10)).thenReturn(Optional.of(cliente));
        when(pedidoRepository.save(any(PedidoEntity.class))).thenThrow(new RuntimeException("boom"));

        PedidoDtoRequest req = new PedidoDtoRequest();
        req.setClienteId(10);

        MensajeDtoResponse resp = pedidoService.guardarPedido(req);

        assertNotNull(resp);
        assertFalse(resp.getExito());
        assertEquals("Error inesperado al guardar pedido", resp.getMensaje());
    }

    @Test
    void actualizarPedido_ok_actualiza_y_devuelveExito() {
        ClienteEntity cliente = clienteEntity(10);
        PedidoEntity existente = pedidoEntity(1, LocalDate.of(2026, 1, 1), 100.0, true, clienteEntity(1));

        when(pedidoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(clienteRepository.findById(10)).thenReturn(Optional.of(cliente));
        when(pedidoRepository.save(existente)).thenReturn(existente);

        PedidoDtoRequest req = new PedidoDtoRequest();
        req.setFechaPedido(LocalDate.of(2026, 2, 1));
        req.setTotal(999.0);
        req.setClienteId(10);

        MensajeDtoResponse resp = pedidoService.actualizarPedido(1, req);

        assertNotNull(resp);
        assertTrue(resp.getExito());
        assertEquals("Pedido actualizado exitosamente", resp.getMensaje());

        verify(pedidoRepository).save(existente);
        assertEquals(LocalDate.of(2026, 2, 1), existente.getFechaPedido());
        assertEquals(999.0, existente.getTotal());
        assertEquals(10, existente.getCliente().getId());
    }

    @Test
    void actualizarPedido_pedidoNoExiste_devuelveError_y_noGuarda() {
        when(pedidoRepository.findById(1)).thenReturn(Optional.empty());

        PedidoDtoRequest req = new PedidoDtoRequest();
        req.setClienteId(10);

        MensajeDtoResponse resp = pedidoService.actualizarPedido(1, req);

        assertNotNull(resp);
        assertFalse(resp.getExito());
        assertEquals("Pedido no encontrado", resp.getMensaje());
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void actualizarPedido_clienteNoExiste_devuelveError_y_noGuarda() {
        PedidoEntity existente = pedidoEntity(1, LocalDate.of(2026, 1, 1), 100.0, true, clienteEntity(1));
        when(pedidoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(clienteRepository.findById(10)).thenReturn(Optional.empty());

        PedidoDtoRequest req = new PedidoDtoRequest();
        req.setClienteId(10);

        MensajeDtoResponse resp = pedidoService.actualizarPedido(1, req);

        assertNotNull(resp);
        assertFalse(resp.getExito());
        assertEquals("Cliente no encontrado", resp.getMensaje());
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void actualizarPedido_siRepositorioFalla_devuelveError() {
        PedidoEntity existente = pedidoEntity(1, LocalDate.of(2026, 1, 1), 100.0, true, clienteEntity(1));
        when(pedidoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(clienteRepository.findById(10)).thenReturn(Optional.of(clienteEntity(10)));
        when(pedidoRepository.save(existente)).thenThrow(new DataAccessResourceFailureException("db down"));

        PedidoDtoRequest req = new PedidoDtoRequest();
        req.setClienteId(10);

        MensajeDtoResponse resp = pedidoService.actualizarPedido(1, req);

        assertNotNull(resp);
        assertFalse(resp.getExito());
        assertEquals("Error de acceso a la base de datos al actualizar pedido", resp.getMensaje());
    }

    @Test
    void actualizarPedido_siOcurreExcepcionInesperada_devuelveError() {
        when(pedidoRepository.findById(1)).thenThrow(new RuntimeException("boom"));

        PedidoDtoRequest req = new PedidoDtoRequest();
        req.setClienteId(10);

        MensajeDtoResponse resp = pedidoService.actualizarPedido(1, req);

        assertNotNull(resp);
        assertFalse(resp.getExito());
        assertEquals("Error inesperado al actualizar pedido", resp.getMensaje());
    }

    @Test
    void eliminarPedido_cuandoExiste_eliminaLogico_y_devuelveExito() {
        when(pedidoRepository.existsById(1)).thenReturn(true);

        MensajeDtoResponse resp = pedidoService.eliminarPedido(1);

        assertNotNull(resp);
        assertTrue(resp.getExito());
        assertEquals("Pedido eliminado exitosamente", resp.getMensaje());
        verify(pedidoRepository).eliminarPedido(1);
    }

    @Test
    void eliminarPedido_cuandoNoExiste_devuelveError_y_noElimina() {
        when(pedidoRepository.existsById(99)).thenReturn(false);

        MensajeDtoResponse resp = pedidoService.eliminarPedido(99);

        assertNotNull(resp);
        assertFalse(resp.getExito());
        assertEquals("Pedido no encontrado", resp.getMensaje());
        verify(pedidoRepository, never()).eliminarPedido(anyInt());
    }

    @Test
    void eliminarPedido_siRepositorioFalla_devuelveError() {
        when(pedidoRepository.existsById(1)).thenThrow(new DataAccessResourceFailureException("db down"));

        MensajeDtoResponse resp = pedidoService.eliminarPedido(1);

        assertNotNull(resp);
        assertFalse(resp.getExito());
        assertEquals("Error de acceso a la base de datos al eliminar pedido", resp.getMensaje());
    }

    @Test
    void eliminarPedido_siOcurreExcepcionInesperada_devuelveError() {
        when(pedidoRepository.existsById(1)).thenThrow(new RuntimeException("boom"));

        MensajeDtoResponse resp = pedidoService.eliminarPedido(1);

        assertNotNull(resp);
        assertFalse(resp.getExito());
        assertEquals("Error inesperado al eliminar pedido", resp.getMensaje());
    }

    @Test
    void buscarPorRangoFechas_filtraInactivos_y_mapeaADto() {
        LocalDate ini = LocalDate.of(2026, 1, 1);
        LocalDate fin = LocalDate.of(2026, 1, 31);
        PedidoEntity activo = pedidoEntity(1, ini, 100.0, true, clienteEntity(10));
        PedidoEntity inactivo = pedidoEntity(2, fin, 200.0, false, clienteEntity(10));
        when(pedidoRepository.findByFechaPedidoBetween(ini, fin)).thenReturn(List.of(activo, inactivo));

        List<PedidoDtoResponse> resultado = pedidoService.buscarPorRangoFechas(ini, fin);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(100.0, resultado.get(0).getTotal());
    }

    @Test
    void buscarPorCliente_filtraInactivos_y_mapeaADto() {
        PedidoEntity activo = pedidoEntity(1, LocalDate.of(2026, 1, 1), 100.0, true, clienteEntity(10));
        PedidoEntity inactivo = pedidoEntity(2, LocalDate.of(2026, 1, 2), 200.0, false, clienteEntity(10));
        when(pedidoRepository.findByCliente_Id(10)).thenReturn(List.of(activo, inactivo));

        List<PedidoDtoResponse> resultado = pedidoService.buscarPorCliente(10);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(10, resultado.get(0).getClienteId());
    }

    @Test
    void buscarPorRangoTotal_mapeaADto() {
        PedidoEntity p1 = pedidoEntity(1, LocalDate.of(2026, 1, 1), 100.0, true, clienteEntity(10));
        when(pedidoRepository.buscarPorRangoTotal(50.0, 150.0)).thenReturn(List.of(p1));

        List<PedidoDtoResponse> resultado = pedidoService.buscarPorRangoTotal(50.0, 150.0);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(100.0, resultado.get(0).getTotal());
    }

    @Test
    void buscarPedidosActivosPorCliente_mapeaADto() {
        PedidoEntity p1 = pedidoEntity(1, LocalDate.of(2026, 1, 1), 100.0, true, clienteEntity(10));
        when(pedidoRepository.buscarPedidosActivosPorCliente(10)).thenReturn(List.of(p1));

        List<PedidoDtoResponse> resultado = pedidoService.buscarPedidosActivosPorCliente(10);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(10, resultado.get(0).getClienteId());
    }

    @Test
    void busquedasPersonalizadas_siRepositorioFalla_devuelvenListasVacias() {
        LocalDate ini = LocalDate.of(2026, 1, 1);
        LocalDate fin = LocalDate.of(2026, 1, 31);

        when(pedidoRepository.findByFechaPedidoBetween(ini, fin)).thenThrow(new DataAccessResourceFailureException("db down"));
        assertTrue(pedidoService.buscarPorRangoFechas(ini, fin).isEmpty());

        reset(pedidoRepository);
        when(pedidoRepository.findByCliente_Id(10)).thenThrow(new RuntimeException("boom"));
        assertTrue(pedidoService.buscarPorCliente(10).isEmpty());

        reset(pedidoRepository);
        when(pedidoRepository.buscarPorRangoTotal(1.0, 2.0)).thenThrow(new DataAccessResourceFailureException("db down"));
        assertTrue(pedidoService.buscarPorRangoTotal(1.0, 2.0).isEmpty());

        reset(pedidoRepository);
        when(pedidoRepository.buscarPedidosActivosPorCliente(10)).thenThrow(new RuntimeException("boom"));
        assertTrue(pedidoService.buscarPedidosActivosPorCliente(10).isEmpty());
    }

    @Test
    void cubreCatchFaltantes_enPedidoService() {
        when(pedidoRepository.findById(1)).thenThrow(new RuntimeException("boom"));
        assertNull(pedidoService.buscarPorId(1));

        reset(pedidoRepository);
        LocalDate ini = LocalDate.of(2026, 1, 1);
        LocalDate fin = LocalDate.of(2026, 1, 31);
        when(pedidoRepository.findByFechaPedidoBetween(ini, fin)).thenThrow(new RuntimeException("boom"));
        assertTrue(pedidoService.buscarPorRangoFechas(ini, fin).isEmpty());

        reset(pedidoRepository);
        when(pedidoRepository.findByCliente_Id(10)).thenThrow(new DataAccessResourceFailureException("db down"));
        assertTrue(pedidoService.buscarPorCliente(10).isEmpty());

        reset(pedidoRepository);
        when(pedidoRepository.buscarPorRangoTotal(1.0, 2.0)).thenThrow(new RuntimeException("boom"));
        assertTrue(pedidoService.buscarPorRangoTotal(1.0, 2.0).isEmpty());

        reset(pedidoRepository);
        when(pedidoRepository.buscarPedidosActivosPorCliente(10)).thenThrow(new DataAccessResourceFailureException("db down"));
        assertTrue(pedidoService.buscarPedidosActivosPorCliente(10).isEmpty());
    }

    private static ClienteEntity clienteEntity(int id) {
        ClienteEntity c = new ClienteEntity();
        c.setId(id);
        return c;
    }

    private static PedidoEntity pedidoEntity(
            Integer id,
            LocalDate fecha,
            Double total,
            boolean activo,
            ClienteEntity cliente
    ) {
        PedidoEntity p = new PedidoEntity();
        p.setId(id);
        p.setFechaPedido(fecha);
        p.setTotal(total);
        p.setActivo(activo);
        p.setCliente(cliente);
        return p;
    }
}
