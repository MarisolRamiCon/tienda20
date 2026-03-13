package com.inndata20.tienda.service.implementacion;

import com.inndata20.tienda.entity.EmpleadoEntity;
import com.inndata20.tienda.model.EmpleadoDtoRequest;
import com.inndata20.tienda.model.EmpleadoDtoResponse;
import com.inndata20.tienda.model.MensajeDtoResponse;
import com.inndata20.tienda.repository.EmpleadoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpleadoServiceTest {

    @Mock
    EmpleadoRepository empleadoRepository;

    @InjectMocks
    EmpleadoService empleadoService;

    @Test
    void listarEmpleados_filtraInactivos_y_mapeaADto() {
        EmpleadoEntity activo = empleadoEntity(1, "Juan", "Perez", "Cajero", true);
        EmpleadoEntity inactivo = empleadoEntity(2, "Ana", "Lopez", "Gerente", false);

        when(empleadoRepository.findAll()).thenReturn(List.of(activo, inactivo));

        List<EmpleadoDtoResponse> resultado = empleadoService.listarEmpleados();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getId());
        assertEquals("Juan", resultado.get(0).getNombre());
        assertEquals("Perez", resultado.get(0).getApellido());
        assertEquals("Cajero", resultado.get(0).getPuesto());
    }

    @Test
    void listarEmpleados_siRepositorioFalla_devuelveListaVacia() {
        when(empleadoRepository.findAll()).thenThrow(new DataAccessResourceFailureException("db down"));

        List<EmpleadoDtoResponse> resultado = empleadoService.listarEmpleados();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void buscarPorId_cuandoExisteYActivo_devuelveDto() {
        EmpleadoEntity empleado = empleadoEntity(1, "Juan", "Perez", "Cajero", true);
        when(empleadoRepository.findById(1)).thenReturn(Optional.of(empleado));

        EmpleadoDtoResponse resultado = empleadoService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("Juan", resultado.getNombre());
    }

    @Test
    void buscarPorId_cuandoNoExiste_o_inactivo_devuelveNull() {
        when(empleadoRepository.findById(1)).thenReturn(Optional.empty());
        assertNull(empleadoService.buscarPorId(1));

        EmpleadoEntity inactivo = empleadoEntity(2, "Ana", "Lopez", "Gerente", false);
        when(empleadoRepository.findById(2)).thenReturn(Optional.of(inactivo));
        assertNull(empleadoService.buscarPorId(2));
    }

    @Test
    void guardarEmpleado_guardaEntidadActiva_y_devuelveExito() {
        EmpleadoDtoRequest req = new EmpleadoDtoRequest();
        req.setNombre("Juan");
        req.setApellido("Perez");
        req.setPuesto("Cajero");
        req.setSalario(new BigDecimal("1234.50"));
        req.setFechaContratacion(LocalDate.of(2026, 1, 10));

        ArgumentCaptor<EmpleadoEntity> captor = ArgumentCaptor.forClass(EmpleadoEntity.class);
        when(empleadoRepository.save(any(EmpleadoEntity.class))).thenAnswer(inv -> inv.getArgument(0, EmpleadoEntity.class));

        MensajeDtoResponse resp = empleadoService.guardarEmpleado(req);

        assertNotNull(resp);
        assertTrue(resp.getExito());
        assertEquals("Empleado guardado exitosamente", resp.getMensaje());

        verify(empleadoRepository).save(captor.capture());
        EmpleadoEntity guardado = captor.getValue();
        assertEquals("Juan", guardado.getNombre());
        assertEquals("Perez", guardado.getApellido());
        assertEquals("Cajero", guardado.getPuesto());
        assertEquals(new BigDecimal("1234.50"), guardado.getSalario());
        assertEquals(LocalDate.of(2026, 1, 10), guardado.getFechaContratacion());
        assertEquals(Boolean.TRUE, guardado.getActivo());
    }

    @Test
    void guardarEmpleado_siRepositorioFalla_devuelveError() {
        EmpleadoDtoRequest req = new EmpleadoDtoRequest();
        req.setNombre("Juan");

        when(empleadoRepository.save(any(EmpleadoEntity.class))).thenThrow(new DataAccessResourceFailureException("db down"));

        MensajeDtoResponse resp = empleadoService.guardarEmpleado(req);

        assertNotNull(resp);
        assertFalse(resp.getExito());
        assertEquals("Error de base de datos al guardar empleado", resp.getMensaje());
    }

    @Test
    void actualizarEmpleado_cuandoExisteYActivo_actualiza_y_devuelveExito() {
        EmpleadoEntity existente = empleadoEntity(1, "Juan", "Perez", "Cajero", true);

        EmpleadoDtoRequest req = new EmpleadoDtoRequest();
        req.setNombre("Pedro");
        req.setApellido("Gomez");
        req.setPuesto("Gerente");
        req.setSalario(new BigDecimal("2000.00"));
        req.setFechaContratacion(LocalDate.of(2025, 12, 1));

        when(empleadoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(empleadoRepository.save(existente)).thenReturn(existente);

        MensajeDtoResponse resp = empleadoService.actualizarEmpleado(1, req);

        assertNotNull(resp);
        assertTrue(resp.getExito());
        assertEquals("Empleado actualizado exitosamente", resp.getMensaje());

        verify(empleadoRepository).save(existente);
        assertEquals("Pedro", existente.getNombre());
        assertEquals("Gomez", existente.getApellido());
        assertEquals("Gerente", existente.getPuesto());
        assertEquals(new BigDecimal("2000.00"), existente.getSalario());
        assertEquals(LocalDate.of(2025, 12, 1), existente.getFechaContratacion());
    }

    @Test
    void actualizarEmpleado_cuandoNoExiste_o_inactivo_devuelveError_y_noGuarda() {
        EmpleadoDtoRequest req = new EmpleadoDtoRequest();
        req.setNombre("Pedro");

        when(empleadoRepository.findById(1)).thenReturn(Optional.empty());
        MensajeDtoResponse respNoExiste = empleadoService.actualizarEmpleado(1, req);
        assertNotNull(respNoExiste);
        assertFalse(respNoExiste.getExito());
        assertEquals("Empleado no encontrado o inactivo", respNoExiste.getMensaje());
        verify(empleadoRepository, never()).save(any());

        reset(empleadoRepository);

        EmpleadoEntity inactivo = empleadoEntity(2, "Ana", "Lopez", "Cajero", false);
        when(empleadoRepository.findById(2)).thenReturn(Optional.of(inactivo));
        MensajeDtoResponse respInactivo = empleadoService.actualizarEmpleado(2, req);
        assertNotNull(respInactivo);
        assertFalse(respInactivo.getExito());
        assertEquals("Empleado no encontrado o inactivo", respInactivo.getMensaje());
        verify(empleadoRepository, never()).save(any());
    }

    @Test
    void eliminarEmpleado_cuandoExiste_eliminaLogico_y_devuelveExito() {
        when(empleadoRepository.existsById(1)).thenReturn(true);

        MensajeDtoResponse resp = empleadoService.eliminarEmpleado(1);

        assertNotNull(resp);
        assertTrue(resp.getExito());
        assertEquals("Empleado eliminado exitosamente", resp.getMensaje());
        verify(empleadoRepository).eliminarEmpleado(1);
    }

    @Test
    void eliminarEmpleado_cuandoNoExiste_devuelveError_y_noElimina() {
        when(empleadoRepository.existsById(99)).thenReturn(false);

        MensajeDtoResponse resp = empleadoService.eliminarEmpleado(99);

        assertNotNull(resp);
        assertFalse(resp.getExito());
        assertEquals("Empleado no encontrado", resp.getMensaje());
        verify(empleadoRepository, never()).eliminarEmpleado(anyInt());
    }

    @Test
    void buscarPorPuesto_filtraInactivos_y_mapeaADto() {
        EmpleadoEntity activo = empleadoEntity(1, "Juan", "Perez", "Cajero", true);
        EmpleadoEntity inactivo = empleadoEntity(2, "Ana", "Lopez", "Cajero", false);
        when(empleadoRepository.findByPuesto("Cajero")).thenReturn(List.of(activo, inactivo));

        List<EmpleadoDtoResponse> resultado = empleadoService.buscarPorPuesto("Cajero");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());
    }

    @Test
    void buscarPorNombre_filtraInactivos_y_mapeaADto() {
        EmpleadoEntity activo = empleadoEntity(1, "Juan", "Perez", "Cajero", true);
        EmpleadoEntity inactivo = empleadoEntity(2, "Juana", "Lopez", "Cajero", false);
        when(empleadoRepository.findByNombreContainingIgnoreCase("juan")).thenReturn(List.of(activo, inactivo));

        List<EmpleadoDtoResponse> resultado = empleadoService.buscarPorNombre("juan");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getId());
    }

    @Test
    void buscarPorRangoSalario_mapeaADto() {
        EmpleadoEntity e1 = empleadoEntity(1, "Juan", "Perez", "Cajero", true);
        e1.setSalario(new BigDecimal("1000.00"));
        EmpleadoEntity e2 = empleadoEntity(2, "Ana", "Lopez", "Gerente", true);
        e2.setSalario(new BigDecimal("1500.00"));

        when(empleadoRepository.buscarPorRangoSalario(new BigDecimal("900.00"), new BigDecimal("1600.00")))
                .thenReturn(List.of(e1, e2));

        List<EmpleadoDtoResponse> resultado =
                empleadoService.buscarPorRangoSalario(new BigDecimal("900.00"), new BigDecimal("1600.00"));

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(new BigDecimal("1000.00"), resultado.get(0).getSalario());
    }

    @Test
    void buscarPorFechaContratacion_mapeaADto() {
        EmpleadoEntity e1 = empleadoEntity(1, "Juan", "Perez", "Cajero", true);
        e1.setFechaContratacion(LocalDate.of(2026, 1, 1));
        when(empleadoRepository.buscarPorFechaContratacion(LocalDate.of(2026, 1, 1))).thenReturn(List.of(e1));

        List<EmpleadoDtoResponse> resultado = empleadoService.buscarPorFechaContratacion(LocalDate.of(2026, 1, 1));

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(LocalDate.of(2026, 1, 1), resultado.get(0).getFechaContratacion());
    }

    private static EmpleadoEntity empleadoEntity(
            Integer id,
            String nombre,
            String apellido,
            String puesto,
            boolean activo
    ) {
        EmpleadoEntity e = new EmpleadoEntity();
        e.setId(id);
        e.setNombre(nombre);
        e.setApellido(apellido);
        e.setPuesto(puesto);
        e.setActivo(activo);
        return e;
    }
}

