package com.inndata20.tienda.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProveedoresRequest {
    private String nombreDeLaEmpresa;
    private String contacto;
    private String telefono;
    private String correo;
}
