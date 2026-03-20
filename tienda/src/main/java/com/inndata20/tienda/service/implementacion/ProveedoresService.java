package com.inndata20.tienda.service.implementacion;


import com.inndata20.tienda.entity.ProveedoresEntity;
import com.inndata20.tienda.model.MensajeStrResponse;
import com.inndata20.tienda.model.ProveedoresRequest;
import com.inndata20.tienda.model.ProveedoresResponse;
import com.inndata20.tienda.repository.ProveedoresRepository;
import com.inndata20.tienda.service.IProveedoresService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service

public class ProveedoresService implements IProveedoresService {

    private final ProveedoresRepository proveedoresRepository;

    public ProveedoresService(ProveedoresRepository proveedoresRepository) {
        this.proveedoresRepository = proveedoresRepository;
    }

    @Override
    public List<ProveedoresResponse> readAll() {
        log.info("Solicitando lista de proveedores");
        try{
            return proveedoresRepository.findByActivoTrue().stream().map(
                    proveedor ->
                            new ProveedoresResponse(
                                    proveedor.getId(),
                                    proveedor.getNombreDeLaEmpresa(),
                                    proveedor.getContacto(),
                                    proveedor.getTelefono(),
                                    proveedor.getCorreo()
                            )
            ).toList();
        } catch(Exception e){
            log.error(e.getMessage());
            throw new EntityNotFoundException(e);
        }
    }

    @Override
    public ProveedoresResponse readById(int id) {
        log.info("Solicitando proveedor con id: {id}");
        try{
            return proveedoresRepository.findById(id).map(proveedor ->
                new ProveedoresResponse(
                        proveedor.getId(),
                        proveedor.getNombreDeLaEmpresa(),
                        proveedor.getContacto(),
                        proveedor.getTelefono(),
                        proveedor.getCorreo()
                )
            ).orElseThrow();
        } catch (Exception e) {
            throw new EntityNotFoundException(e);
        }
    }

    @Override
    public MensajeStrResponse create(ProveedoresRequest entrada) {
        log.info("Solicitando crear proveedor");
        ProveedoresEntity nuevoProveedor = new ProveedoresEntity();
        nuevoProveedor.setNombreDeLaEmpresa(entrada.getNombreDeLaEmpresa());
        nuevoProveedor.setContacto(entrada.getContacto());
        nuevoProveedor.setTelefono(entrada.getTelefono());
        nuevoProveedor.setCorreo(entrada.getCorreo());

        try{
            log.info("Creando proveedor");
            proveedoresRepository.save(nuevoProveedor);
            return new MensajeStrResponse("Proveedor registrado exitosamente");
        }  catch(Exception e){
            log.error(e.getMessage());
            return new MensajeStrResponse("Error al registrar el proveedor");
        }
    }

    @Override
    public MensajeStrResponse updateById(int id, ProveedoresRequest proveedoresRequest) {
        log.info("Solicitando actualizar proveedor con id: {id}");
        try{
            ProveedoresEntity proveedorExistente = proveedoresRepository.findById(id).orElseThrow();
            proveedorExistente.setNombreDeLaEmpresa(proveedoresRequest.getNombreDeLaEmpresa());
            proveedorExistente.setContacto(proveedoresRequest.getContacto());
            proveedorExistente.setTelefono(proveedoresRequest.getTelefono());
            proveedorExistente.setCorreo(proveedoresRequest.getCorreo());

            proveedoresRepository.save(proveedorExistente);
            return new MensajeStrResponse("Proveedor actualizado exitosamente");
        } catch (Exception e){
            log.error(e.getMessage());
            return new MensajeStrResponse("Error al actualizar el proveedor");
        }
    }

    @Override
    public MensajeStrResponse deleteById(int id) {
        log.info("Solicitando eliminar proveedor con id: {id}");
        try{
            ProveedoresEntity proveedorExistente = proveedoresRepository.findById(id).orElseThrow();
            proveedorExistente.setActivo(false);
            proveedoresRepository.save(proveedorExistente);
            return new MensajeStrResponse("Proveedor eliminado exitosamente");
        } catch (Exception e){
            log.error(e.getMessage());
            return new MensajeStrResponse("Error al eliminar el proveedor");
        }
    }
}
