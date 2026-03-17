package com.inndata20.tienda.service.implementacion;

import com.inndata20.tienda.entity.ClienteEntity;
import com.inndata20.tienda.model.ClienteDtoRequest;
import com.inndata20.tienda.model.ClienteDtoResponse;
import com.inndata20.tienda.model.MensajeStrResponse;
import com.inndata20.tienda.repository.ClienteRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    ClienteRepository clienteRepository;

    ClienteEntity cliente1;
    ClienteEntity cliente2;
    ClienteEntity cliente3;
    ClienteEntity cliente4;

    @InjectMocks
    ClienteService clienteService;

    @BeforeEach
    void setUp() {
        cliente1 = new ClienteEntity(1,"Alfa","Sigma","Aqui","alpha.sigma@correo.com","1234567890",true);
        cliente2 = new ClienteEntity(2,"Beta","Sigma","Alla","alpha.sigma@correo.com","1234567890",true);
        cliente3 = new ClienteEntity(3,"Gamma","Sigma","Acuya","alpha.sigma@correo.com","1234567890",false);
        cliente4 = new ClienteEntity(4,"Delta","Sigma","Hasta alla","alpha.sigma@correo.com","1234567890",true);
    }

    @Test
    void readAll() {
        List<ClienteEntity> data = Arrays.asList(cliente1, cliente2, cliente4);
        Mockito.when(clienteRepository.findByActivoTrue()).thenReturn(data);
        List<ClienteDtoResponse> salida = clienteService.readAll();
        assertEquals(3,salida.size());
    }

    @Test
    void readById() {
        Mockito.when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente1));
        Optional<ClienteDtoResponse> salida = clienteService.readById(1);

        assertTrue(salida.isPresent());
        assertEquals("Alfa",salida.get().getNombre());
        verify(clienteRepository).findById(1);
    }

    @Test
    void create() {
        Mockito.when(clienteRepository.save(any(ClienteEntity.class))).thenReturn(cliente1);
        ClienteDtoRequest clienteNuevo = new ClienteDtoRequest();
        clienteNuevo.setNombre("Epsilon");
        clienteNuevo.setApellido("Sigma");
        clienteNuevo.setDireccion("Porai");
        clienteNuevo.setTelefono("1234567890");
        MensajeStrResponse salida = clienteService.create(clienteNuevo);
        assertEquals("Cliente registrado exitosamente",salida.getMensaje());

    }

    @Test
    void update() {
        Mockito.when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente1));
        Mockito.when(clienteRepository.save(any(ClienteEntity.class))).thenReturn(cliente1);
        ClienteDtoRequest clienteActualizado = new ClienteDtoRequest();
        clienteActualizado.setNombre("Epsilon");
        clienteActualizado.setApellido("Sigma");
        clienteActualizado.setDireccion("Porai");
        clienteActualizado.setTelefono("1234567890");
        MensajeStrResponse salida = clienteService.updateById(1,clienteActualizado);
        assertEquals("Cliente modificado exitosamente",salida.getMensaje());
    }

    @Test
    void delete() {
        Mockito.when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente1));
        Mockito.when(clienteRepository.save(any(ClienteEntity.class))).thenReturn(cliente1);
        MensajeStrResponse salida = clienteService.deleteById(1);
        assertEquals("Cliente eliminado exitosamente",salida.getMensaje());
    }

    @Test
    void searchByNombre() {
        List<ClienteEntity> data = Arrays.asList(cliente1, cliente2);
        Mockito.when(clienteRepository.searchByNombre("Al")).thenReturn(data);
        List<ClienteDtoResponse> salida = clienteService.searchByName("Al");
        assertEquals(2, salida.size());
    }

    @Test
    void readAllException() {
        Mockito.when(clienteRepository.findByActivoTrue()).thenThrow(new RuntimeException("Error al obtener clientes"));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            clienteService.readAll();
        });
        assertEquals("Error al obtener clientes", exception.getCause().getMessage());
    }

    @Test
    void readByIdException() {
        Mockito.when(clienteRepository.findById(1)).thenThrow(new RuntimeException("Error al obtener cliente por id"));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            clienteService.readById(1);
        });
        assertEquals("Error al obtener cliente por id", exception.getCause().getMessage());
    }

    @Test
    void createException() {
        Mockito.when(clienteRepository.save(any(ClienteEntity.class))).thenThrow(new RuntimeException("Error al crear cliente"));

        ClienteDtoRequest clienteNuevo = new ClienteDtoRequest();
        clienteNuevo.setCorreo("test@example.com");

        MensajeStrResponse response = clienteService.create(clienteNuevo);

        assertEquals("Error al crear cliente", response.getMensaje());
    }

    @Test
    void updateException() {
        Mockito.when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente1));
        Mockito.when(clienteRepository.save(any(ClienteEntity.class)))
                .thenThrow(new RuntimeException("Error al actualizar cliente por id"));

        ClienteDtoRequest clienteActualizado = new ClienteDtoRequest();
        clienteActualizado.setNombre("Epsilon");

        MensajeStrResponse response = clienteService.updateById(1, clienteActualizado);

        assertEquals("Error al actualizar cliente por id", response.getMensaje());
        verify(clienteRepository).findById(1);
        verify(clienteRepository).save(any(ClienteEntity.class));
    }

    @Test
    void deleteException() {
        Mockito.when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente1));
        Mockito.when(clienteRepository.save(any(ClienteEntity.class)))
                .thenThrow(new RuntimeException("Error al eliminar cliente por id"));

        MensajeStrResponse response = clienteService.deleteById(1);

        assertEquals("Error al eliminar cliente por id", response.getMensaje());
        verify(clienteRepository).findById(1);
        verify(clienteRepository).save(any(ClienteEntity.class));
    }

    @Test
    void searchByNombreException() {
        Mockito.when(clienteRepository.searchByNombre("Al")).thenThrow(new RuntimeException("Error al buscar clientes por nombre"));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            clienteService.searchByName("Al");
        });
        assertEquals("Error al buscar clientes por nombre", exception.getCause().getMessage());
    }

    @Test
    void updateNotFound() {
        Mockito.when(clienteRepository.findById(1)).thenReturn(Optional.empty());
        ClienteDtoRequest clienteActualizado = new ClienteDtoRequest();
        clienteActualizado.setNombre("Epsilon");
        MensajeStrResponse response = clienteService.updateById(1, clienteActualizado);
        assertEquals("El cliente no existe", response.getMensaje());
        verify(clienteRepository).findById(1);
    }

    @Test
    void updateFindException() {
        Mockito.when(clienteRepository.findById(1)).thenThrow(new RuntimeException("Error al buscar cliente por id"));
        ClienteDtoRequest clienteActualizado = new ClienteDtoRequest();
        clienteActualizado.setNombre("Epsilon");
        Exception exception = assertThrows(RuntimeException.class, () -> {
            clienteService.updateById(1, clienteActualizado);
        });
        assertEquals("Error al buscar cliente por id", exception.getCause().getMessage());
        verify(clienteRepository).findById(1);
    }

    @Test
    void deleteNotFound() {
        Mockito.when(clienteRepository.findById(1)).thenReturn(Optional.empty());
        MensajeStrResponse response = clienteService.deleteById(1);
        assertEquals("El cliente no se encontró en el registro", response.getMensaje());
        verify(clienteRepository).findById(1);
    }

    @Test
    void createInvalidData() {
        ClienteDtoRequest clienteNuevo = new ClienteDtoRequest();
        MensajeStrResponse response = clienteService.create(clienteNuevo);
        assertEquals("El cliente debe registrar un correo y/o un numero de teléfono", response.getMensaje());
    }
}