package com.inndata20.tienda.service.implementacion;

import com.inndata20.tienda.entity.EmpleadoEntity;
import com.inndata20.tienda.repository.EmpleadoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)



class EmpleadoServiceTest {

    @Mock
    EmpleadoRepository empleadoRepository; // ✅ Aquí

    @InjectMocks
    EmpleadoService empleadoService;

    @Test
    void listarEmpleados() {

        // Arrange
        when(empleadoRepository.findAll()).thenReturn(List.of());

        // Act
        List<EmpleadoEntity> resultado = empleadoService.listarEmpleados();

        // Assert
        assertNotNull(resultado);

    }

    @Test
    void buscarPorId() {

        // Arrange
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setId(1);
        empleado.setNombre("Juan");

        when(empleadoRepository.findById(1)).thenReturn(Optional.of(empleado));

        // Act
        EmpleadoEntity resultado = empleadoService.buscarPorId(1);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
    }


    @Test
    void guardarEmpleado() {

        // Arrange
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setNombre("Juan");
        empleado.setApellido("Pérez");

        when(empleadoRepository.save(empleado)).thenReturn(empleado);

        // Act
        EmpleadoEntity resultado = empleadoService.guardarEmpleado(empleado);

        // Assert
        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());



    }

    @Test
    void actualizarEmpleado() {

        // Arrange
        EmpleadoEntity empleadoExistente = new EmpleadoEntity();
        empleadoExistente.setId(1);
        empleadoExistente.setNombre("Juan");

        EmpleadoEntity empleadoActualizado = new EmpleadoEntity();
        empleadoActualizado.setNombre("Pedro");

        when(empleadoRepository.findById(1)).thenReturn(Optional.of(empleadoExistente));
        when(empleadoRepository.save(empleadoExistente)).thenReturn(empleadoExistente);

        // Act
        EmpleadoEntity resultado = empleadoService.actualizarEmpleado(1, empleadoActualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("Pedro", resultado.getNombre());


    }

    @Test
    void eliminarEmpleado() {

            // Arrange - caso existe
            when(empleadoRepository.existsById(1)).thenReturn(true);
            // Arrange - caso no existe
            when(empleadoRepository.existsById(99)).thenReturn(false);

            // Act y Assert - caso existe
            assertTrue(empleadoService.eliminarEmpleado(1));

            // Act y Assert - caso no existe
            assertFalse(empleadoService.eliminarEmpleado(99));
        }


    }
