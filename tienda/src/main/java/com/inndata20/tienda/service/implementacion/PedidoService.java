package com.inndata20.tienda.service.implementacion;

import com.inndata20.tienda.entity.ClienteEntity;
import com.inndata20.tienda.entity.PedidoEntity;
import com.inndata20.tienda.model.PedidoDtoRequest;
import com.inndata20.tienda.model.PedidoDtoResponse;
import com.inndata20.tienda.repository.ClienteRepository;
import com.inndata20.tienda.repository.PedidoRepository;
import com.inndata20.tienda.service.IPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService implements IPedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;

    @Autowired

    public PedidoService(PedidoRepository pedidoRepository, ClienteRepository clienteRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public List<PedidoDtoResponse> listarPedidos() {
        return pedidoRepository.findAll()
                .stream()
                .filter(PedidoEntity::getActivo)
                .map(pedido -> {
                    PedidoDtoResponse dto = new PedidoDtoResponse();
                    dto.setFechaPedido(pedido.getFechaPedido());
                    dto.setTotal(pedido.getTotal());
                    dto.setClienteId(pedido.getCliente().getId());
                    return dto;
                })
                .toList();
    }

    @Override
    public PedidoDtoResponse buscarPorId(Integer id) {
        return pedidoRepository.findById(id)
                .filter(PedidoEntity::getActivo)
                .map(pedido -> {
                    PedidoDtoResponse dto = new PedidoDtoResponse();
                    dto.setFechaPedido(pedido.getFechaPedido());
                    dto.setTotal(pedido.getTotal());
                    dto.setClienteId(pedido.getCliente().getId());
                    return dto;
                })
                .orElse(null);
    }

    @Override
    public String guardarPedido(PedidoDtoRequest dto) {

        try {

            ClienteEntity cliente = clienteRepository.findById(dto.getClienteId()).orElse(null);
            if (cliente == null) return "Cliente no encontrado";

            PedidoEntity pedido = new PedidoEntity();
            pedido.setFechaPedido(dto.getFechaPedido());
            pedido.setTotal(dto.getTotal());
            pedido.setActivo(true);
            pedido.setCliente(cliente);
            pedidoRepository.save(pedido);
            return "Pedido creado exitosamente";
        } catch (DataAccessException e){
            throw new PedidoServiceException("Error de acceso a la base de datos al guardar pedido", e);
        } catch (Exception e) {
            throw new PedidoServiceException("Error inesperado al guardar pedido", e);
        }
    }


    @Override
    public String actualizarPedido(Integer id, PedidoDtoRequest dto) {
        try {
            PedidoEntity pedidoExistente = pedidoRepository.findById(id).orElse(null);
            if (pedidoExistente == null) return "Pedido no encontrado";

            ClienteEntity cliente = clienteRepository.findById(dto.getClienteId()).orElse(null);
            if (cliente == null) return "Cliente no encontrado";

            pedidoExistente.setFechaPedido(dto.getFechaPedido());
            pedidoExistente.setTotal(dto.getTotal());
            pedidoExistente.setCliente(cliente);

            pedidoRepository.save(pedidoExistente);
            return "Pedido actualizado exitosamente";

        } catch (DataAccessException e) {
            throw new PedidoServiceException("Error de acceso a la base de datos al actualizar pedido con id " + id, e);
        } catch (Exception e) {
            throw new PedidoServiceException("Error inesperado al actualizar pedido con id " + id, e);
        }
    }

    public class PedidoServiceException extends RuntimeException {
        public PedidoServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    @Override
    public boolean eliminarPedido(Integer id) {

        try {

            if (pedidoRepository.existsById(id)) {
                pedidoRepository.eliminarPedido(id);
                return true;
            }
            return false;
        } catch (DataAccessException e) {

            throw new PedidoServiceException("Error de acceso a la base de datos", e);

        } catch (Exception e) {

            throw new PedidoServiceException("Error inesperado al eliminar pedido con id " + id, e);


        }
    }
}