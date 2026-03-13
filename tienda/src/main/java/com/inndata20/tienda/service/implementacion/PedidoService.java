package com.inndata20.tienda.service.implementacion;

import com.inndata20.tienda.entity.ClienteEntity;
import com.inndata20.tienda.entity.PedidoEntity;
import com.inndata20.tienda.model.MensajeDtoResponse;
import com.inndata20.tienda.model.PedidoDtoRequest;
import com.inndata20.tienda.model.PedidoDtoResponse;
import com.inndata20.tienda.repository.ClienteRepository;
import com.inndata20.tienda.repository.PedidoRepository;
import com.inndata20.tienda.service.IPedidoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
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
        log.info("Service: Consultando todos los pedidos activos en la base de datos");
        try {
            return pedidoRepository.findAll()
                    .stream()
                    .filter(PedidoEntity::getActivo)
                    .map(pedido -> {
                        PedidoDtoResponse pedidoRequest = new PedidoDtoResponse();
                        pedidoRequest.setFechaPedido(pedido.getFechaPedido());
                        pedidoRequest.setTotal(pedido.getTotal());
                        pedidoRequest.setClienteId(pedido.getCliente().getId());
                        return pedidoRequest;
                    })
                    .toList();
        } catch (DataAccessException e) {
            log.error("Service: Error de BD al consultar pedidos", e);
            return List.of();
        } catch (Exception e) {
            log.error("Service: Error inesperado al consultar pedidos", e);
            return List.of();
        }
    }

    @Override
    public PedidoDtoResponse buscarPorId(Integer id) {
        log.info("Service: Buscando pedido por ID: {}", id);
        try {
            PedidoDtoResponse response = pedidoRepository.findById(id)
                    .filter(PedidoEntity::getActivo)
                    .map(pedido -> {
                        PedidoDtoResponse dto = new PedidoDtoResponse();
                        dto.setFechaPedido(pedido.getFechaPedido());
                        dto.setTotal(pedido.getTotal());
                        dto.setClienteId(pedido.getCliente().getId());
                        return dto;
                    })
                    .orElse(null);

            if (response == null) {
                log.warn("Service: Pedido con ID {} no encontrado o está inactivo", id);
            } else {
                log.info("Service: Pedido con ID {} encontrado con éxito", id);
            }
            return response;
        } catch (DataAccessException e) {
            log.error("Service: Error de BD al buscar pedido con ID {}", id, e);
            return null;
        } catch (Exception e) {
            log.error("Service: Error inesperado al buscar pedido con ID {}", id, e);
            return null;
        }
    }

    @Transactional
    @Override
    public MensajeDtoResponse guardarPedido(PedidoDtoRequest pedidoRequest) {
        log.info("Service: Iniciando proceso para guardar nuevo pedido del cliente ID: {}", pedidoRequest.getClienteId());
        try {
            ClienteEntity cliente = clienteRepository.findById(pedidoRequest.getClienteId()).orElse(null);
            if (cliente == null) {
                log.warn("Service: Error al guardar. No se encontró el cliente con ID: {}", pedidoRequest.getClienteId());
                return new MensajeDtoResponse("Error: Cliente no encontrado ", false);
            }

            PedidoEntity pedido = new PedidoEntity();
            pedido.setFechaPedido(pedidoRequest.getFechaPedido());
            pedido.setTotal(pedidoRequest.getTotal());
            pedido.setActivo(true);
            pedido.setCliente(cliente);
            pedidoRepository.save(pedido);
            log.info("Service: Pedido guardado exitosamente para el cliente ID: {}", pedidoRequest.getClienteId());
            return new MensajeDtoResponse("Pedido guardado exitosamente", true);
        } catch (DataAccessException e) {
            log.error("Service: Error de BD al intentar guardar el pedido", e);
            return new MensajeDtoResponse("Error de acceso a la base de datos al guardar pedido", false);
        } catch (Exception e) {
            log.error("Service: Error inesperado al intentar guardar el pedido", e);
            return new MensajeDtoResponse("Error inesperado al guardar pedido", false);
        }
    }

    @Transactional
    @Override
    public MensajeDtoResponse actualizarPedido(Integer id, PedidoDtoRequest pedidoRequest) {
        log.info("Service: Iniciando proceso de actualización para el pedido ID: {}", id);
        try {
            PedidoEntity pedidoExistente = pedidoRepository.findById(id).orElse(null);
            if (pedidoExistente == null) {
                log.warn("Service: No se puede actualizar. Pedido con ID {} no encontrado", id);
                return new MensajeDtoResponse("Pedido no encontrado", false);
            }

            ClienteEntity cliente = clienteRepository.findById(pedidoRequest.getClienteId()).orElse(null);
            if (cliente == null) {
                log.warn("Service: No se puede actualizar. Cliente con ID {} no encontrado", pedidoRequest.getClienteId());
                return new MensajeDtoResponse("Cliente no encontrado", false);
            }

            pedidoExistente.setFechaPedido(pedidoRequest.getFechaPedido());
            pedidoExistente.setTotal(pedidoRequest.getTotal());
            pedidoExistente.setCliente(cliente);

            pedidoRepository.save(pedidoExistente);
            log.info("Service: Pedido con ID {} actualizado exitosamente", id);
            return new MensajeDtoResponse("Pedido actualizado exitosamente", true);

        } catch (DataAccessException e) {
            log.error("Service: Error de BD al intentar actualizar el pedido ID {}", id, e);
            return new MensajeDtoResponse("Error de acceso a la base de datos al actualizarpedido", false);
        } catch (Exception e) {
            log.error("Service: Error inesperado al actualizar el pedido ID {}", id, e);
            return new MensajeDtoResponse("Error inesperado al actualizar pedido con id " + id, false);
        }
    }

    @Transactional
    @Override
    public MensajeDtoResponse eliminarPedido(Integer id) {
        log.info("Service: Solicitud para eliminar lógicamente el pedido con ID: {}", id);
        try {
            if (pedidoRepository.existsById(id)) {
                pedidoRepository.eliminarPedido(id);
                log.info("Service: Pedido con ID {} eliminado lógicamente de la BD", id);
                return new MensajeDtoResponse("Pedido eliminado exitosamente", true);
            }
            log.warn("Service: No se pudo eliminar. Pedido con ID {} no existe", id);
            return new MensajeDtoResponse("Pedido no encontrado", false);
        } catch (DataAccessException e) {
            log.error("Service: Error de BD al eliminar el pedido ID {}", id, e);
            return new MensajeDtoResponse("Error de acceso a la base de datos al eliminar pedido con id " + id, false);
        } catch (Exception e) {
            log.error("Service: Error inesperado al eliminar el pedido ID {}", id, e);
            return new MensajeDtoResponse("Error inesperado al eliminar pedido con id " + id, false);
        }
    }

    // METODOS PERSONALIZADOS JPA

    @Override
    public List<PedidoDtoResponse> buscarPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        log.info("Service: Consultando BD por pedidos entre {} y {}", fechaInicio, fechaFin);
        try {
            return pedidoRepository.findByFechaPedidoBetween(fechaInicio, fechaFin)
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
        } catch (DataAccessException e) {
            log.error("Service: Error de BD al buscar por rango de fechas", e);
            return List.of();
        } catch (Exception e) {
            log.error("Service: Error inesperado al buscar por rango de fechas", e);
            return List.of();
        }
    }

    @Override
    public List<PedidoDtoResponse> buscarPorCliente(Integer clienteId) {
        log.info("Service: Consultando BD por pedidos del cliente ID: {}", clienteId);
        try {
            return pedidoRepository.findByCliente_Id(clienteId)
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
        } catch (DataAccessException e) {
            log.error("Service: Error de BD al buscar por cliente ID: {}", clienteId, e);
            return List.of();
        } catch (Exception e) {
            log.error("Service: Error inesperado al buscar por cliente ID: {}", clienteId, e);
            return List.of();
        }
    }

// QUERYS PERSONALIZADOS

    @Override
    public List<PedidoDtoResponse> buscarPorRangoTotal(Double rangoMin, Double rangoMax) {
        log.info("Service: Consultando BD por pedidos con total entre {} y {}", rangoMin, rangoMax);
        try {
            return pedidoRepository.buscarPorRangoTotal(rangoMin, rangoMax)
                    .stream()
                    .map(pedido -> {
                        PedidoDtoResponse dto = new PedidoDtoResponse();
                        dto.setFechaPedido(pedido.getFechaPedido());
                        dto.setTotal(pedido.getTotal());
                        dto.setClienteId(pedido.getCliente().getId());
                        return dto;
                    })
                    .toList();
        } catch (DataAccessException e) {
            log.error("Service: Error de BD al buscar por rango de total", e);
            return List.of();
        } catch (Exception e) {
            log.error("Service: Error inesperado al buscar por rango de total", e);
            return List.of();
        }
    }

    @Override
    public List<PedidoDtoResponse> buscarPedidosActivosPorCliente(Integer clienteId) {
        log.info("Service: Consultando BD por pedidos activos del cliente ID: {}", clienteId);
        try {
            return pedidoRepository.buscarPedidosActivosPorCliente(clienteId)
                    .stream()
                    .map(pedido -> {
                        PedidoDtoResponse dto = new PedidoDtoResponse();
                        dto.setFechaPedido(pedido.getFechaPedido());
                        dto.setTotal(pedido.getTotal());
                        dto.setClienteId(pedido.getCliente().getId());
                        return dto;
                    })
                    .toList();
        } catch (DataAccessException e) {
            log.error("Service: Error de BD al buscar pedidos activos del cliente ID: {}", clienteId, e);
            return List.of();
        } catch (Exception e) {
            log.error("Service: Error inesperado al buscar pedidos activos del cliente ID: {}", clienteId, e);
            return List.of();
        }
    }

    // Clase interna para manejo de excepciones
    public class PedidoServiceException extends RuntimeException {
        public PedidoServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }


}