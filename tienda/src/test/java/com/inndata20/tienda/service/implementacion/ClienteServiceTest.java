package com.inndata20.tienda.service.implementacion;

import com.inndata20.tienda.entity.ClienteEntity;
import com.inndata20.tienda.model.ClienteDtoRequest;
import com.inndata20.tienda.model.ClienteDtoResponse;
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
        Mockito.when(clienteRepository.findAll()).thenReturn(Arrays.asList(cliente1,cliente2,cliente3,cliente4));
        List<ClienteDtoResponse> salida =  clienteService.readAll();
        assertEquals(3,salida.size());
    }

    @Test
    void readById() {
        Mockito.when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente1));
        Optional<ClienteDtoResponse> salida = clienteService.readById(1);
        assertEquals(salida,clienteService.readById(1));
    }

    @Test
    void create() {
        Mockito.when(clienteRepository.save(Mockito.any(ClienteEntity.class))).thenReturn(cliente1);
        ClienteDtoRequest clienteNuevo = new ClienteDtoRequest();
        clienteNuevo.setNombre("Epsilon");
        clienteNuevo.setApellido("Sigma");
        clienteNuevo.setDireccion("Porai");
        clienteNuevo.setTelefono("1234567890");
        ClienteDtoRequest clienteErroneo = new ClienteDtoRequest();
        String salida = clienteService.create(clienteNuevo);
        assertEquals("Cliente registrado exitosamente",salida);

    }
}