package com.inndata20.tienda.feign;

// LOOMBOK

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data


public class APIMock {
    private String productos; // El "id" que genera MockAPI
    private String nombre;
    private String descripcion;
    private String precio; // ✅ Viene como String no como Double
    private String categoria;
    private Boolean stock;


}
