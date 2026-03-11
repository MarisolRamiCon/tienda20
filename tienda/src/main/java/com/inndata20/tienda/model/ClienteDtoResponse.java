package com.inndata20.tienda.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ClienteDtoResponse {
    private int id;
    private String nombre;
    private String apellido;
    private String direccion;
    private String correo;
    private String telefono;
}
