package com.inndata20.tienda.service.implementacion;


import com.inndata20.tienda.entity.ClienteEntity;
import com.inndata20.tienda.model.ClienteDtoRequest;
import com.inndata20.tienda.model.ClienteDtoResponse;
import com.inndata20.tienda.repository.ClienteRepository;
import com.inndata20.tienda.service.IClienteService;
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
        return clienteRepository.findByActivoNotNull().stream().map(
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
    }

    @Override
    public Optional<ClienteDtoResponse> readById(int id){
        log.info("Solicitando el cliente por id: {id}");
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
    }

    @Override
    public String create(ClienteDtoRequest cliente){
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
                return "Cliente registrado exitosamente";
            } catch (Exception e) {
                return e.getMessage();
            }
        } else  {
            return "El cliente debe registrar un correo y/o un numero de teléfono";
        }
    }

    @Override
    public String updateById(int id, ClienteDtoRequest entrada){
        log.info("Solicitando la modificación del cliente por id: {id}");
        Optional<ClienteEntity> solicitud = clienteRepository.findById(id);
        if (solicitud.isPresent()) {
            ClienteEntity cliente = solicitud.get();

            if (entrada.getNombre() != null) cliente.setNombre(entrada.getNombre());
            if (entrada.getApellido() != null) cliente.setApellido(entrada.getApellido());
            if (entrada.getDireccion() != null) cliente.setDireccion(entrada.getDireccion());
            if (entrada.getCorreo() != null) cliente.setCorreo(entrada.getCorreo());
            if (entrada.getTelefono() != null) cliente.setTelefono(entrada.getTelefono());

            try {
                clienteRepository.save(cliente);
                return "Cliente modificado exitosamente";
            } catch (Exception e) {
                return e.getMessage();
            }
        }else  {
            return "El cliente no existe";
        }
    }

    @Override
    public String deleteById(int id){
        log.info("Solicitando la eliminación del cliente por id: {id}");
        Optional<ClienteEntity> solicitud = clienteRepository.findById(id);
        if (solicitud.isPresent()) {
            ClienteEntity cliente = solicitud.get();
            cliente.setActivo(false);
            clienteRepository.save(cliente);
            return "Cliente eliminado exitosamente";
        } else {
            return "El cliente no se encontró el registro";
        }
    }

    public List<ClienteDtoResponse> searchByName(String busqueda){
        return clienteRepository.findByNombre(busqueda).stream().map(cliente ->
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
