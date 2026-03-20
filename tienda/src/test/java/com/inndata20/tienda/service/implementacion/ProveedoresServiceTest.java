package com.inndata20.tienda.service.implementacion;

import com.inndata20.tienda.entity.ProveedoresEntity;
import com.inndata20.tienda.model.MensajeStrResponse;
import com.inndata20.tienda.model.ProveedoresRequest;
import com.inndata20.tienda.model.ProveedoresResponse;
import com.inndata20.tienda.repository.ProveedoresRepository;
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
class ProveedoresServiceTest {

    @Mock
    ProveedoresRepository proveedoresRepository;

    ProveedoresEntity proveedor1;
    ProveedoresEntity proveedor2;
    ProveedoresEntity proveedor3;
    ProveedoresEntity proveedor4;

    @InjectMocks
    ProveedoresService proveedoresService;

    @BeforeEach
    void setUp() {
        proveedor1 = new ProveedoresEntity(1, "TechSupplies Inc", "Juan García", "555-1234", "juan@techsupplies.com", true);
        proveedor2 = new ProveedoresEntity(2, "Global Components Ltd", "María López", "555-5678", "maria@globalcomp.com", true);
        proveedor3 = new ProveedoresEntity(3, "Hardware World", "Carlos Martínez", "555-9012", "carlos@hwworld.com", false);
        proveedor4 = new ProveedoresEntity(4, "Electronics Plus", "Ana Rodríguez", "555-3456", "ana@eplus.com", true);
    }

    @Test
    void readAll() {
        List<ProveedoresEntity> data = Arrays.asList(proveedor1, proveedor2, proveedor4);
        Mockito.when(proveedoresRepository.findByActivoTrue()).thenReturn(data);

        List<ProveedoresResponse> salida = proveedoresService.readAll();
        assertEquals(3, salida.size());
    }

    @Test
    void readById() {
        Mockito.when(proveedoresRepository.findById(1)).thenReturn(Optional.of(proveedor1));

        ProveedoresResponse salida = proveedoresService.readById(1);
        assertNotNull(salida);
        assertEquals("TechSupplies Inc", salida.getNombreDeLaEmpresa());
    }

    @Test
    void create() {
        Mockito.when(proveedoresRepository.save(Mockito.any(ProveedoresEntity.class))).thenReturn(proveedor1);

        ProveedoresRequest proveedorNuevo = new ProveedoresRequest();
        proveedorNuevo.setNombreDeLaEmpresa("TechSupplies Inc");
        proveedorNuevo.setContacto("Juan García");
        proveedorNuevo.setTelefono("555-1234");
        proveedorNuevo.setCorreo("juan@techsupplies.com");

        MensajeStrResponse salida = proveedoresService.create(proveedorNuevo);
        assertEquals("Proveedor registrado exitosamente", salida.getMensaje());
    }

    @Test
    void updateById() {
        Mockito.when(proveedoresRepository.findById(1)).thenReturn(Optional.of(proveedor1));
        Mockito.when(proveedoresRepository.save(Mockito.any(ProveedoresEntity.class))).thenReturn(proveedor1);

        ProveedoresRequest proveedorActualizado = new ProveedoresRequest();
        proveedorActualizado.setNombreDeLaEmpresa("TechSupplies Inc Actualizado");
        proveedorActualizado.setContacto("Juan García Updated");
        proveedorActualizado.setTelefono("555-1111");
        proveedorActualizado.setCorreo("juan@newtechsupplies.com");

        MensajeStrResponse salida = proveedoresService.updateById(1, proveedorActualizado);
        assertEquals("Proveedor actualizado exitosamente", salida.getMensaje());
    }

    @Test
    void deleteById() {
        Mockito.when(proveedoresRepository.findById(1)).thenReturn(Optional.of(proveedor1));
        Mockito.when(proveedoresRepository.save(Mockito.any(ProveedoresEntity.class))).thenReturn(proveedor1);

        MensajeStrResponse salida = proveedoresService.deleteById(1);
        assertEquals("Proveedor eliminado exitosamente", salida.getMensaje());
    }
}