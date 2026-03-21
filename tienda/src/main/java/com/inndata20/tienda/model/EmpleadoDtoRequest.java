package com.inndata20.tienda.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EmpleadoDtoRequest {
    private Integer id;
    private String nombre;
    private String apellido;
    private String puesto;
    private BigDecimal salario;
    private LocalDate fechaContratacion;
}