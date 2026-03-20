package com.inndata20.tienda.service.implementacion;

import com.inndata20.tienda.feign.ForaneoClient;
import com.inndata20.tienda.feign.ForaneoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ForaneoServiceTest {

    @Mock
    ForaneoClient foraneoClient;

    ForaneoEntity foraneo1;
    ForaneoEntity foraneo2;
    ForaneoEntity foraneo3;

    @InjectMocks
    ForaneoService foraneoService;

    @BeforeEach
    void setUp() {
        foraneo1 = new ForaneoEntity(1, "Juan Pérez", "Empresa A");
        foraneo2 = new ForaneoEntity(2, "María García", "Empresa B");
        foraneo3 = new ForaneoEntity(3, "Carlos López", "Empresa C");
    }

    @Test
    void readAll() {
        List<ForaneoEntity> data = Arrays.asList(foraneo1, foraneo2, foraneo3);
        Mockito.when(foraneoClient.readAll()).thenReturn(data);
        
        List<ForaneoEntity> salida = foraneoService.readAll();
        assertEquals(3, salida.size());
        assertEquals("Juan Pérez", salida.get(0).getName());
    }

    @Test
    void readById() {
        Mockito.when(foraneoClient.readById(1)).thenReturn(foraneo1);
        
        ForaneoEntity salida = foraneoService.readById(1);
        assertEquals(1, salida.getId());
        assertEquals("Juan Pérez", salida.getName());
    }

    @Test
    void create() {
        ForaneoEntity nuevoForaneo = new ForaneoEntity(4, "Ana Martínez", "Empresa D");
        Mockito.when(foraneoClient.create(Mockito.any(ForaneoEntity.class))).thenReturn(nuevoForaneo);
        
        ForaneoEntity salida = foraneoService.create(nuevoForaneo);
        assertEquals("Ana Martínez", salida.getName());
        assertEquals("Empresa D", salida.getCompany());
    }

    @Test
    void update() {
        ForaneoEntity foraneoActualizado = new ForaneoEntity(1, "Juan Pérez Actualizado", "Empresa A Actualizada");
        Mockito.when(foraneoClient.update(1, foraneoActualizado)).thenReturn(foraneoActualizado);
        
        ForaneoEntity salida = foraneoService.update(1, foraneoActualizado);
        assertEquals("Juan Pérez Actualizado", salida.getName());
    }

    @Test
    void delete() {
        Mockito.doNothing().when(foraneoClient).delete(1);
        
        assertDoesNotThrow(() -> foraneoService.delete(1));
        Mockito.verify(foraneoClient, Mockito.times(1)).delete(1);
    }
}