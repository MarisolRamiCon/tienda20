package com.inndata20.tienda.service.implementacion;


import com.inndata20.tienda.entity.ClienteEntity;
import com.inndata20.tienda.model.ClienteDtoRequest;
import com.inndata20.tienda.model.ClienteDtoResponse;
import com.inndata20.tienda.repository.ClienteRepository;
import com.inndata20.tienda.service.IClienteService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class ClienteService implements IClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public List<ClienteDtoResponse> readAll() {
        return clienteRepository.findAll().stream().filter(ClienteEntity::isActivo).map(
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
            return "El cliente debe registrar un correo y/o un numero de telefono";
        }
    }
}
