package com.inndata20.tienda.service.implementacion;

import com.inndata20.tienda.entity.ProductoEntity;
import com.inndata20.tienda.entity.ProveedoresEntity;
import com.inndata20.tienda.model.MensajeDtoResponse;
import com.inndata20.tienda.model.ProductoDtoRequest;
import com.inndata20.tienda.model.ProductoDtoResponse;
import com.inndata20.tienda.repository.ProductoRepository;
import com.inndata20.tienda.repository.ProveedoresRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    ProductoRepository productoRepository;

    @Mock
    ProveedoresRepository proveedoresRepository;

    @InjectMocks
    ProductoService productoService;

    @Test
    void listarProductos_filtraInactivos_y_mapeaADto() {
        ProveedoresEntity prov = proveedorEntity(1);
        ProductoEntity activo = productoEntity(1, "Laptop", "Desc", 1000.0, "Tech", 5, true, prov);
        ProductoEntity inactivo = productoEntity(2, "Mouse", "Desc", 10.0, "Tech", 50, false, prov);
        when(productoRepository.findAll()).thenReturn(List.of(activo, inactivo));

        List<ProductoDtoResponse> resultado = productoService.listarProductos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Laptop", resultado.get(0).getNombre());
        assertEquals(1, resultado.get(0).getProveedor());
    }

    @Test
    void listarProductos_siRepositorioFalla_devuelveListaVacia() {
        when(productoRepository.findAll()).thenThrow(new DataAccessResourceFailureException("db down"));

        List<ProductoDtoResponse> resultado = productoService.listarProductos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void listarProductos_siOcurreExcepcionInesperada_devuelveListaVacia() {
        ProductoEntity activoConNpe = productoEntity(1, "Laptop", "Desc", 1000.0, "Tech", 5, true, proveedorEntity(1));
        // Fuerza NPE por autounboxing en el filter(ProductoEntity::getActivo)
        activoConNpe.setActivo(null);
        when(productoRepository.findAll()).thenReturn(List.of(activoConNpe));

        List<ProductoDtoResponse> resultado = productoService.listarProductos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void buscarPorId_cuandoExisteYActivo_devuelveDto() {
        ProductoEntity producto = productoEntity(1, "Laptop", "Desc", 1000.0, "Tech", 5, true, proveedorEntity(1));
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));

        ProductoDtoResponse resultado = productoService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals("Laptop", resultado.getNombre());
        assertEquals(1, resultado.getProveedor());
    }

    @Test
    void buscarPorId_cuandoNoExiste_o_inactivo_devuelveNull() {
        when(productoRepository.findById(1)).thenReturn(Optional.empty());
        assertNull(productoService.buscarPorId(1));

        ProductoEntity inactivo = productoEntity(2, "Mouse", "Desc", 10.0, "Tech", 50, false, proveedorEntity(1));
        when(productoRepository.findById(2)).thenReturn(Optional.of(inactivo));
        assertNull(productoService.buscarPorId(2));
    }

    @Test
    void buscarPorId_siRepositorioFalla_devuelveNull() {
        when(productoRepository.findById(1)).thenThrow(new DataAccessResourceFailureException("db down"));
        assertNull(productoService.buscarPorId(1));
    }

    @Test
    void buscarPorId_cuandoProveedorEsNull_noFalla_y_dejaProveedorNull() {
        ProductoEntity producto = productoEntity(1, "Laptop", "Desc", 1000.0, "Tech", 5, true, null);
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));

        ProductoDtoResponse resultado = productoService.buscarPorId(1);

        assertNotNull(resultado);
        assertNull(resultado.getProveedor());
    }

    @Test
    void guardarProducto_cuandoProveedorExiste_guarda_y_devuelveExito() {
        ProveedoresEntity prov = proveedorEntity(1);
        when(proveedoresRepository.findById(1)).thenReturn(Optional.of(prov));
        when(productoRepository.save(any(ProductoEntity.class))).thenAnswer(inv -> inv.getArgument(0, ProductoEntity.class));

        ProductoDtoRequest req = new ProductoDtoRequest();
        req.setNombre("Laptop");
        req.setDescripcion("Desc");
        req.setPrecio(1000.0);
        req.setCategoria("Tech");
        req.setStock(5);
        req.setProveedor(1);

        ArgumentCaptor<ProductoEntity> captor = ArgumentCaptor.forClass(ProductoEntity.class);
        MensajeDtoResponse resp = productoService.guardarProducto(req);

        assertNotNull(resp);
        assertTrue(resp.getExito());
        assertEquals("Producto guardado exitosamente", resp.getMensaje());

        verify(productoRepository).save(captor.capture());
        ProductoEntity guardado = captor.getValue();
        assertEquals("Laptop", guardado.getNombre());
        assertEquals("Desc", guardado.getDescripcion());
        assertEquals(1000.0, guardado.getPrecio());
        assertEquals("Tech", guardado.getCategoria());
        assertEquals(5, guardado.getStock());
        assertEquals(Boolean.TRUE, guardado.getActivo());
        assertEquals(1, guardado.getProveedor().getId());
    }

    @Test
    void guardarProducto_cuandoProveedorNoExiste_devuelveError_y_noGuarda() {
        when(proveedoresRepository.findById(1)).thenReturn(Optional.empty());

        ProductoDtoRequest req = new ProductoDtoRequest();
        req.setNombre("Laptop");
        req.setProveedor(1);

        MensajeDtoResponse resp = productoService.guardarProducto(req);

        assertNotNull(resp);
        assertFalse(resp.getExito());
        assertEquals("Proveedor no encontrado", resp.getMensaje());
        verify(productoRepository, never()).save(any());
    }

    @Test
    void guardarProducto_siRepositorioFalla_devuelveError() {
        ProveedoresEntity prov = proveedorEntity(1);
        when(proveedoresRepository.findById(1)).thenReturn(Optional.of(prov));
        when(productoRepository.save(any(ProductoEntity.class))).thenThrow(new DataAccessResourceFailureException("db down"));

        ProductoDtoRequest req = new ProductoDtoRequest();
        req.setNombre("Laptop");
        req.setProveedor(1);

        MensajeDtoResponse resp = productoService.guardarProducto(req);

        assertNotNull(resp);
        assertFalse(resp.getExito());
        assertEquals("Error de base de datos al guardar producto", resp.getMensaje());
    }

    @Test
    void guardarProducto_siOcurreExcepcionInesperada_devuelveError() {
        ProveedoresEntity prov = proveedorEntity(1);
        when(proveedoresRepository.findById(1)).thenReturn(Optional.of(prov));
        when(productoRepository.save(any(ProductoEntity.class))).thenThrow(new RuntimeException("boom"));

        ProductoDtoRequest req = new ProductoDtoRequest();
        req.setNombre("Laptop");
        req.setProveedor(1);

        MensajeDtoResponse resp = productoService.guardarProducto(req);

        assertNotNull(resp);
        assertFalse(resp.getExito());
        assertEquals("Error inesperado al guardar producto", resp.getMensaje());
    }

    @Test
    void actualizarProducto_ok_actualiza_y_devuelveExito() {
        ProveedoresEntity prov = proveedorEntity(1);
        ProductoEntity existente = productoEntity(1, "Laptop", "Desc", 1000.0, "Tech", 5, true, prov);

        when(productoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(proveedoresRepository.findById(1)).thenReturn(Optional.of(prov));
        when(productoRepository.save(existente)).thenReturn(existente);

        ProductoDtoRequest req = new ProductoDtoRequest();
        req.setNombre("Laptop Lenovo");
        req.setDescripcion("Nueva");
        req.setPrecio(1500.0);
        req.setCategoria("Tech");
        req.setStock(7);
        req.setProveedor(1);

        MensajeDtoResponse resp = productoService.actualizarProducto(1, req);

        assertNotNull(resp);
        assertTrue(resp.getExito());
        assertEquals("Producto actualizado exitosamente", resp.getMensaje());

        verify(productoRepository).save(existente);
        assertEquals("Laptop Lenovo", existente.getNombre());
        assertEquals("Nueva", existente.getDescripcion());
        assertEquals(1500.0, existente.getPrecio());
        assertEquals(7, existente.getStock());
        assertEquals(1, existente.getProveedor().getId());
    }

    @Test
    void actualizarProducto_productoNoExiste_devuelveError_y_noGuarda() {
        when(productoRepository.findById(1)).thenReturn(Optional.empty());

        ProductoDtoRequest req = new ProductoDtoRequest();
        req.setProveedor(1);

        MensajeDtoResponse resp = productoService.actualizarProducto(1, req);

        assertNotNull(resp);
        assertFalse(resp.getExito());
        assertEquals("Producto no encontrado", resp.getMensaje());
        verify(productoRepository, never()).save(any());
    }

    @Test
    void actualizarProducto_proveedorNoExiste_devuelveError_y_noGuarda() {
        ProductoEntity existente = productoEntity(1, "Laptop", "Desc", 1000.0, "Tech", 5, true, proveedorEntity(1));
        when(productoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(proveedoresRepository.findById(99)).thenReturn(Optional.empty());

        ProductoDtoRequest req = new ProductoDtoRequest();
        req.setProveedor(99);

        MensajeDtoResponse resp = productoService.actualizarProducto(1, req);

        assertNotNull(resp);
        assertFalse(resp.getExito());
        assertEquals("Proveedor no encontrado", resp.getMensaje());
        verify(productoRepository, never()).save(any());
    }

    @Test
    void actualizarProducto_siRepositorioFalla_devuelveError() {
        ProveedoresEntity prov = proveedorEntity(1);
        ProductoEntity existente = productoEntity(1, "Laptop", "Desc", 1000.0, "Tech", 5, true, prov);
        when(productoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(proveedoresRepository.findById(1)).thenReturn(Optional.of(prov));
        when(productoRepository.save(existente)).thenThrow(new DataAccessResourceFailureException("db down"));

        ProductoDtoRequest req = new ProductoDtoRequest();
        req.setProveedor(1);

        MensajeDtoResponse resp = productoService.actualizarProducto(1, req);

        assertNotNull(resp);
        assertFalse(resp.getExito());
        assertEquals("Error de base de datos al actualizar producto", resp.getMensaje());
    }

    @Test
    void actualizarProducto_siOcurreExcepcionInesperada_devuelveError() {
        when(productoRepository.findById(1)).thenThrow(new RuntimeException("boom"));

        ProductoDtoRequest req = new ProductoDtoRequest();
        req.setProveedor(1);

        MensajeDtoResponse resp = productoService.actualizarProducto(1, req);

        assertNotNull(resp);
        assertFalse(resp.getExito());
        assertEquals("Error inesperado al actualizar producto", resp.getMensaje());
    }

    @Test
    void eliminarProducto_cuandoExiste_eliminaLogico_y_devuelveExito() {
        when(productoRepository.existsById(1)).thenReturn(true);

        MensajeDtoResponse resp = productoService.eliminarProducto(1);

        assertNotNull(resp);
        assertTrue(resp.getExito());
        assertEquals("Producto eliminado exitosamente", resp.getMensaje());
        verify(productoRepository).eliminarProducto(1);
    }

    @Test
    void eliminarProducto_cuandoNoExiste_devuelveError_y_noElimina() {
        when(productoRepository.existsById(99)).thenReturn(false);

        MensajeDtoResponse resp = productoService.eliminarProducto(99);

        assertNotNull(resp);
        assertFalse(resp.getExito());
        assertEquals("Producto no encontrado", resp.getMensaje());
        verify(productoRepository, never()).eliminarProducto(anyInt());
    }

    @Test
    void eliminarProducto_siRepositorioFalla_devuelveError() {
        when(productoRepository.existsById(1)).thenThrow(new DataAccessResourceFailureException("db down"));

        MensajeDtoResponse resp = productoService.eliminarProducto(1);

        assertNotNull(resp);
        assertFalse(resp.getExito());
        assertEquals("Error de base de datos al eliminar producto", resp.getMensaje());
    }

    @Test
    void eliminarProducto_siOcurreExcepcionInesperada_devuelveError() {
        when(productoRepository.existsById(1)).thenThrow(new RuntimeException("boom"));

        MensajeDtoResponse resp = productoService.eliminarProducto(1);

        assertNotNull(resp);
        assertFalse(resp.getExito());
        assertEquals("Error inesperado al eliminar producto", resp.getMensaje());
    }

    @Test
    void buscarPorCategoriaYPrecio_filtraInactivos_y_mapeaADto() {
        ProveedoresEntity prov = proveedorEntity(1);
        ProductoEntity activo = productoEntity(1, "Laptop", "Desc", 1000.0, "Tech", 5, true, prov);
        ProductoEntity inactivo = productoEntity(2, "Mouse", "Desc", 10.0, "Tech", 50, false, prov);
        when(productoRepository.findByCategoriaAndPrecioLessThan("Tech", 2000.0)).thenReturn(List.of(activo, inactivo));

        List<ProductoDtoResponse> resultado = productoService.buscarPorCategoriaYPrecio("Tech", 2000.0);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Laptop", resultado.get(0).getNombre());
    }

    @Test
    void buscarPorStockEntre_filtraInactivos_y_mapeaADto() {
        ProveedoresEntity prov = proveedorEntity(1);
        ProductoEntity activo = productoEntity(1, "Laptop", "Desc", 1000.0, "Tech", 5, true, prov);
        ProductoEntity inactivo = productoEntity(2, "Mouse", "Desc", 10.0, "Tech", 50, false, prov);
        when(productoRepository.findByStockBetween(1, 10)).thenReturn(List.of(activo, inactivo));

        List<ProductoDtoResponse> resultado = productoService.buscarPorStockEntre(1, 10);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(5, resultado.get(0).getStock());
    }

    @Test
    void buscarPorNombreYCategoria_mapeaADto() {
        ProveedoresEntity prov = proveedorEntity(1);
        ProductoEntity activo = productoEntity(1, "Laptop", "Desc", 1000.0, "Tech", 5, true, prov);
        when(productoRepository.buscarPorNombreYCategoria("lap", "Tech")).thenReturn(List.of(activo));

        List<ProductoDtoResponse> resultado = productoService.buscarPorNombreYCategoria("lap", "Tech");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Tech", resultado.get(0).getCategoria());
    }

    @Test
    void buscarPorProveedor_mapeaADto() {
        ProveedoresEntity prov = proveedorEntity(1);
        ProductoEntity activo = productoEntity(1, "Laptop", "Desc", 1000.0, "Tech", 5, true, prov);
        when(productoRepository.buscarPorProveedor(1)).thenReturn(List.of(activo));

        List<ProductoDtoResponse> resultado = productoService.buscarPorProveedor(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getProveedor());
    }

    @Test
    void busquedasPersonalizadas_siRepositorioFalla_devuelvenListasVacias() {
        when(productoRepository.findByCategoriaAndPrecioLessThan("Tech", 1.0))
                .thenThrow(new DataAccessResourceFailureException("db down"));
        assertTrue(productoService.buscarPorCategoriaYPrecio("Tech", 1.0).isEmpty());

        reset(productoRepository);
        when(productoRepository.findByStockBetween(1, 2)).thenThrow(new RuntimeException("boom"));
        assertTrue(productoService.buscarPorStockEntre(1, 2).isEmpty());

        reset(productoRepository);
        when(productoRepository.buscarPorNombreYCategoria("x", "Tech")).thenThrow(new DataAccessResourceFailureException("db down"));
        assertTrue(productoService.buscarPorNombreYCategoria("x", "Tech").isEmpty());

        reset(productoRepository);
        when(productoRepository.buscarPorProveedor(1)).thenThrow(new RuntimeException("boom"));
        assertTrue(productoService.buscarPorProveedor(1).isEmpty());
    }

    @Test
    void cubreCatchFaltantes_enProductoService() {
        when(productoRepository.findById(1)).thenThrow(new RuntimeException("boom"));
        assertNull(productoService.buscarPorId(1));

        reset(productoRepository);
        when(productoRepository.findByCategoriaAndPrecioLessThan("Tech", 1.0)).thenThrow(new RuntimeException("boom"));
        assertTrue(productoService.buscarPorCategoriaYPrecio("Tech", 1.0).isEmpty());

        reset(productoRepository);
        when(productoRepository.findByStockBetween(1, 2)).thenThrow(new DataAccessResourceFailureException("db down"));
        assertTrue(productoService.buscarPorStockEntre(1, 2).isEmpty());

        reset(productoRepository);
        when(productoRepository.buscarPorNombreYCategoria("x", "Tech")).thenThrow(new RuntimeException("boom"));
        assertTrue(productoService.buscarPorNombreYCategoria("x", "Tech").isEmpty());

        reset(productoRepository);
        when(productoRepository.buscarPorProveedor(1)).thenThrow(new DataAccessResourceFailureException("db down"));
        assertTrue(productoService.buscarPorProveedor(1).isEmpty());
    }

    private static ProveedoresEntity proveedorEntity(int id) {
        ProveedoresEntity p = new ProveedoresEntity();
        p.setId(id);
        return p;
    }

    private static ProductoEntity productoEntity(
            Integer id,
            String nombre,
            String descripcion,
            Double precio,
            String categoria,
            Integer stock,
            boolean activo,
            ProveedoresEntity proveedor
    ) {
        ProductoEntity p = new ProductoEntity();
        p.setId(id);
        p.setNombre(nombre);
        p.setDescripcion(descripcion);
        p.setPrecio(precio);
        p.setCategoria(categoria);
        p.setStock(stock);
        p.setActivo(activo);
        p.setProveedor(proveedor);
        return p;
    }
}
