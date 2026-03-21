package com.inndata20.tienda.model;

// DTO para la respuesta de mensajes

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class MensajeDtoResponse {

    private String mensaje;
    private Boolean exito;

}
