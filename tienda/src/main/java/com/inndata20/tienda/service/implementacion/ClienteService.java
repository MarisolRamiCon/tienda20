package com.inndata20.tienda.service.implementacion;


import com.inndata20.tienda.entity.ClienteEntity;
import com.inndata20.tienda.model.ClienteDtoRequest;
import com.inndata20.tienda.model.ClienteDtoResponse;
import com.inndata20.tienda.model.MensajeStrResponse;
import com.inndata20.tienda.repository.ClienteRepository;
import com.inndata20.tienda.service.IClienteService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j

public class ClienteService implements IClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public List<ClienteDtoResponse> readAll() {
        log.info("Solicitando lista de clientes");
        try {
            return clienteRepository.findByActivoTrue().stream().map(
                    cliente ->
                            new ClienteDtoResponse(
                                    cliente.getId(),
                                    cliente.getNombre(),
                                    cliente.getApellido(),
                                    cliente.getDireccion(),
                                    cliente.getCorreo(),
                                    cliente.getTelefono()
                            )
            ).toList();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new EntityNotFoundException(e);
        }
    }

    @Override
    public Optional<ClienteDtoResponse> readById(int id){
        log.info("Solicitando el cliente por id: {id}");
        try {
            return clienteRepository.findById(id).map(cliente ->
                    new ClienteDtoResponse(
                            cliente.getId(),
                            cliente.getNombre(),
                            cliente.getApellido(),
                            cliente.getDireccion(),
                            cliente.getCorreo(),
                            cliente.getTelefono()
                    )
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new EntityNotFoundException(e);
        }
    }

    @Override
    public MensajeStrResponse create(ClienteDtoRequest cliente){
        log.info("Creando nuevo registro de cliente");
        ClienteEntity nuevoCliente = new ClienteEntity();
        nuevoCliente.setNombre(cliente.getNombre());
        nuevoCliente.setApellido(cliente.getApellido());
        nuevoCliente.setDireccion(cliente.getDireccion());
        nuevoCliente.setCorreo(cliente.getCorreo());
        nuevoCliente.setTelefono(cliente.getTelefono());

        if (nuevoCliente.getCorreo() != null || nuevoCliente.getTelefono() != null){
            try {
                clienteRepository.save(nuevoCliente);
                return new MensajeStrResponse("Cliente registrado exitosamente");
            } catch (Exception e) {
                //TODO: Dejar log aqui.
                return new MensajeStrResponse(e.getMessage());
            }
        } else  {
            return new MensajeStrResponse("El cliente debe registrar un correo y/o un numero de teléfono");
        }
    }

    @Override
    public MensajeStrResponse updateById(int id, ClienteDtoRequest entrada){
        log.info("Solicitando la modificación del cliente por id: {id}");
        Optional<ClienteEntity> solicitud;
        try {
            log.info("Buscando el cliente por id: {id}");
            solicitud = clienteRepository.findById(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new EntityNotFoundException(e);
        }
        if (solicitud.isPresent()) {
            ClienteEntity cliente = solicitud.get();

            if (entrada.getNombre() != null) cliente.setNombre(entrada.getNombre());
            if (entrada.getApellido() != null) cliente.setApellido(entrada.getApellido());
            if (entrada.getDireccion() != null) cliente.setDireccion(entrada.getDireccion());
            if (entrada.getCorreo() != null) cliente.setCorreo(entrada.getCorreo());
            if (entrada.getTelefono() != null) cliente.setTelefono(entrada.getTelefono());

            try {
                clienteRepository.save(cliente);
                return new MensajeStrResponse("Cliente registrado exitosamente");
            } catch (Exception e) {
                return new MensajeStrResponse(e.getMessage());
            }
        }else  {
            return new MensajeStrResponse("El cliente no existe");
        }
    }

    @Override
    public MensajeStrResponse deleteById(int id){
        log.info("Solicitando la eliminación del cliente por id: {id}");
        Optional<ClienteEntity> solicitud = clienteRepository.findById(id);// TODO: Agregar Try catch
        if (solicitud.isPresent()) {
            ClienteEntity cliente = solicitud.get();
            cliente.setActivo(false);
            clienteRepository.save(cliente);
            return new MensajeStrResponse("Cliente eliminado exitosamente");
        } else {
            return new MensajeStrResponse("El cliente no se encontró el registro");
        }
    }

    public List<ClienteDtoResponse> searchByName(String busqueda){
        return clienteRepository.searchByNombre(busqueda).stream().map(cliente -> //TODO: Agregar Try catch
                new ClienteDtoResponse(
                        cliente.getId(),
                        cliente.getNombre(),
                        cliente.getApellido(),
                        cliente.getDireccion(),
                        cliente.getCorreo(),
                        cliente.getTelefono()
                )
        ).toList();
    }
}
