package com.inndata20.tienda.controller;

import com.inndata20.tienda.model.ClienteDtoRequest;
import com.inndata20.tienda.model.ClienteDtoResponse;
import com.inndata20.tienda.service.implementacion.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Clientes", description = "Endpoints para gestionar clientes")
public class ClienteControllerExample {

    private final ClienteService clienteService;

    public ClienteControllerExample(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/clientes")
    @Operation(
        summary = "Obtener todos los clientes",
        description = "Retorna una lista completa de todos los clientes registrados en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de clientes obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDtoResponse.class))
        ),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public List<ClienteDtoResponse> getAll() {
        log.info("Obteniendo todos los clientes");
        return clienteService.readAll();
    }

    @GetMapping("/clientes/{id}")
    @Operation(
        summary = "Obtener cliente por ID",
        description = "Retorna los detalles de un cliente específico usando su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Cliente encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDtoResponse.class))
        ),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ClienteDtoResponse getById(@PathVariable Integer id) {
        log.info("Obteniendo cliente con ID: {}", id);
        return clienteService.readById(id);
    }

    @PostMapping("/clientes")
    @Operation(
        summary = "Crear un nuevo cliente",
        description = "Crea un nuevo cliente en el sistema con los datos proporcionados"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Cliente creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDtoResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ClienteDtoResponse create(
        @RequestBody(description = "Datos del cliente a crear", required = true,
                     content = @Content(schema = @Schema(implementation = ClienteDtoRequest.class)))
        ClienteDtoRequest clienteDtoRequest
    ) {
        log.info("Creando nuevo cliente: {}", clienteDtoRequest);
        return clienteService.create(clienteDtoRequest);
    }

    @PutMapping("/clientes/{id}")
    @Operation(
        summary = "Actualizar un cliente",
        description = "Actualiza los datos de un cliente existente"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Cliente actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDtoResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ClienteDtoResponse update(
        @PathVariable Integer id,
        @RequestBody(description = "Datos actualizados del cliente", required = true,
                     content = @Content(schema = @Schema(implementation = ClienteDtoRequest.class)))
        ClienteDtoRequest clienteDtoRequest
    ) {
        log.info("Actualizando cliente con ID: {} con datos: {}", id, clienteDtoRequest);
        return clienteService.update(id, clienteDtoRequest);
    }

    @DeleteMapping("/clientes/{id}")
    @Operation(
        summary = "Eliminar un cliente",
        description = "Elimina un cliente del sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public void delete(@PathVariable Integer id) {
        log.info("Eliminando cliente con ID: {}", id);
        clienteService.delete(id);
    }
}

