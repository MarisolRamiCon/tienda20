package com.inndata20.tienda.service.implementacion;


import com.inndata20.tienda.entity.ClienteEntity;
import com.inndata20.tienda.model.ClienteDtoRequest;
import com.inndata20.tienda.repository.ClienteRepository;
import com.inndata20.tienda.service.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class ClienteService implements IClienteService {
    @Autowired
    ClienteRepository clienteRepository;

    @Override
    public List<ClienteEntity> readAll() {
        return clienteRepository.findAll();
    }

    @Override
    public Optional<ClienteEntity> readById(int id){
        return clienteRepository.findById(id);
    }

    @Override
    public String create(ClienteDtoRequest cliente){
        ClienteEntity nuevoCliente = new ClienteEntity();
        nuevoCliente.setNombre(cliente.getNombre());
        nuevoCliente.setApellido(cliente.getApellido());
        nuevoCliente.setDireccion(cliente.getDireccion());
        nuevoCliente.setCorreo(cliente.getCorreo());
        nuevoCliente.setTelefono(cliente.getTelefono());
        clienteRepository.save(nuevoCliente);
        return "Cliente Registrado exitosamente";
    }
}
