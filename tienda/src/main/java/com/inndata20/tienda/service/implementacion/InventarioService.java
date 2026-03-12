package com.inndata20.tienda.service.implementacion;

import com.inndata20.tienda.entity.InventarioEntity;
import com.inndata20.tienda.entity.ProductoEntity;
import com.inndata20.tienda.model.InventarioRequest;
import com.inndata20.tienda.model.InventarioResponse;
import com.inndata20.tienda.model.MensajeStrResponse;
import com.inndata20.tienda.repository.InventarioRepository;
import com.inndata20.tienda.repository.ProductoRepository;
import com.inndata20.tienda.service.IInventarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service

public class InventarioService implements IInventarioService {

    private final InventarioRepository inventarioRepository;
    private final ProductoRepository productoRepository;

    public InventarioService(InventarioRepository inventarioRepository, ProductoRepository productoRepository) {
        this.inventarioRepository = inventarioRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public List<InventarioResponse> readAll() {
        return inventarioRepository.findByActivoTrue().stream().map(inventario -> new InventarioResponse(
                inventario.getId(),
                productoRepository.findById(inventario.getProducto().getId()).orElseThrow(),
                inventario.getCantidadStock()
        )).toList();
    }

    @Override
    public InventarioResponse readById(int id) {
        return inventarioRepository.findById(id).map(inventario -> new InventarioResponse(
                inventario.getId(),
                productoRepository.findById(inventario.getProducto().getId()).orElseThrow(),
                inventario.getCantidadStock()
        )).orElseThrow();
    }

    @Override
    public MensajeStrResponse create(InventarioRequest inventarioRequest) {
        log.info("Creando nuevo inventario");
        InventarioEntity nuevo = new InventarioEntity();
        ProductoEntity producto = productoRepository.findById(inventarioRequest.getProducto()).orElseThrow();
        nuevo.setProducto(producto);
        nuevo.setCantidadStock(inventarioRequest.getCantidadStock());
        try{
            log.info("Guardando nuevo inventario");
            inventarioRepository.save(nuevo);
            return new MensajeStrResponse("Inventario creado exitosamente");
        } catch (Exception e){
            log.error(e.getMessage());
            return new MensajeStrResponse("Error al crear el inventario");
        }
    }

    @Override
    public MensajeStrResponse updateById(int id, InventarioRequest entrada) {
        log.info("Actualizando inventario con id: {id}");
        Optional<InventarioEntity> solicitud = inventarioRepository.findById(id);
        if (solicitud.isPresent()){
            InventarioEntity inventario = solicitud.get();
            inventario.setCantidadStock(entrada.getCantidadStock());
            inventario.setProducto(productoRepository.findById(entrada.getProducto()).orElseThrow());
            try{
                log.info("Guardando inventario actualizado");
                inventarioRepository.save(inventario);
                return new MensajeStrResponse("Inventario actualizado exitosamente");
            } catch (Exception e){
                log.error(e.getMessage());
                return new MensajeStrResponse("Error al actualizar el inventario");
            }
        } else {
            return new MensajeStrResponse("Inventario no encontrado");
        }
    }

    @Override
    public MensajeStrResponse deleteById(int id) {
        log.info("Eliminando inventario con id: {id}");
        Optional<InventarioEntity> solicitud = inventarioRepository.findById(id);
        if (solicitud.isPresent()) {
            InventarioEntity inventario = solicitud.get();
            inventario.setActivo(false);
            try {
                log.info("Guardando inventario eliminado");
                inventarioRepository.save(inventario);
                return new MensajeStrResponse("Inventario eliminado exitosamente");
            } catch (Exception e) {
                log.error(e.getMessage());
                return new MensajeStrResponse("Error al eliminar el inventario");
            }
        } else {
            return new MensajeStrResponse("Inventario no encontrado");
        }
    }

}
