package com.inndata20.tienda.service.implementacion;

import com.inndata20.tienda.entity.DetallePedidoEntity;
import com.inndata20.tienda.entity.PedidoEntity;
import com.inndata20.tienda.entity.ProductoEntity;
import com.inndata20.tienda.model.DetallePedidoRequest;
import com.inndata20.tienda.model.DetallePedidoResponse;
import com.inndata20.tienda.model.MensajeStrResponse;
import com.inndata20.tienda.repository.DetallePedidoRepository;
import com.inndata20.tienda.repository.PedidoRepository;
import com.inndata20.tienda.repository.ProductoRepository;
import com.inndata20.tienda.service.IDetallePedidoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j

public class DetallePedidoService implements IDetallePedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final DetallePedidoRepository detallePedidoRepository;

    public DetallePedidoService(DetallePedidoRepository detallePedidoRepository, PedidoRepository pedidoRepository, ProductoRepository productoRepository) {
        this.detallePedidoRepository = detallePedidoRepository;
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public List<DetallePedidoResponse> readAll() {
        log.info("Solicitando lista de detalles de pedido");
        return detallePedidoRepository.findByActivoTrue().stream().map(
                detalle ->
                        new DetallePedidoResponse(
                                detalle.getId(),
                                pedidoRepository.findById(detalle.getPedido().getId()),
                                productoRepository.findById(detalle.getProducto().getId()),
                                detalle.getCantidad(),
                                detalle.getPrecioUnitario()
                        )
        ).toList();
    }

    @Override
    public Optional<DetallePedidoResponse> readById(int id) {
        log.info("Solicitando detalles de un pedido");
        return detallePedidoRepository.findById(id).map(detalle ->
                        new DetallePedidoResponse(
                                detalle.getId(),
                                pedidoRepository.findById(detalle.getPedido().getId()),
                                productoRepository.findById(detalle.getProducto().getId()),
                                detalle.getCantidad(),
                                detalle.getPrecioUnitario()
                        )
        );
    }

    @Override
    public MensajeStrResponse create(DetallePedidoRequest detallePedido){
        log.info("Creando detalles de un pedido");
        DetallePedidoEntity nuevoDetallePedido = new DetallePedidoEntity();
        PedidoEntity pedido = pedidoRepository.findById(detallePedido.getProducto()).orElseThrow();
        ProductoEntity producto = productoRepository.findById(detallePedido.getProducto()).orElseThrow();
        nuevoDetallePedido.setProducto(producto);
        nuevoDetallePedido.setPedido(pedido);
        nuevoDetallePedido.setCantidad(detallePedido.getCantidad());
        nuevoDetallePedido.setPrecioUnitario(detallePedido.getPrecioUnitario());
        try{
            log.info("Guardando nuevo pedido");
            detallePedidoRepository.save(nuevoDetallePedido);
            return new MensajeStrResponse("Detalle registrado exitosamente");
        } catch (Exception e) {
            log.error(e.getMessage());
            return new MensajeStrResponse(e.getMessage());
        }
    }

    @Override
    public MensajeStrResponse updateById(int id, DetallePedidoRequest entrada) {
        log.info("Modificando detalles de un pedido");
        Optional<DetallePedidoEntity> solicitud = detallePedidoRepository.findById(id);
        if (solicitud.isPresent()) {
            DetallePedidoEntity detallePedido = solicitud.get();
            detallePedido.setCantidad(entrada.getCantidad());
            detallePedido.setProducto(productoRepository.findById(entrada.getProducto()).orElseThrow());
            detallePedido.setPedido(pedidoRepository.findById(entrada.getPedido()).orElseThrow());
            detallePedido.setPrecioUnitario(entrada.getPrecioUnitario());

            try{
                log.info("Guardando modificaciones de detalle de un pedido");
                detallePedidoRepository.save(detallePedido);
                return new MensajeStrResponse("Detalle registrado exitosamente");
            }  catch (Exception e) {
                log.info(e.getMessage());
                return new MensajeStrResponse(e.getMessage());
            }
        }else{
            return new MensajeStrResponse("El detalle no existe");
        }
    }

    @Override
    public MensajeStrResponse deleteById(int id) {
        log.info("Eliminando detalles de un pedido");
        Optional<DetallePedidoEntity> solicitud = detallePedidoRepository.findById(id);
        if (solicitud.isPresent()) {
            DetallePedidoEntity detallePedido = solicitud.get();
            detallePedido.setActivo(false);
            try {
                detallePedidoRepository.save(detallePedido);
            } catch (Exception e) {
                log.error(e.getMessage());
                return new MensajeStrResponse(e.getMessage());
            }
            return new MensajeStrResponse("Detalle eliminado exitosamente");
        } else {
            return new MensajeStrResponse("El detalle no existe");
        }
    }
}
