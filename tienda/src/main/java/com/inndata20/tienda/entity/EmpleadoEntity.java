package com.inndata20.tienda.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "empleados")

// LOOMBOK

@AllArgsConstructor
@NoArgsConstructor
@Data

public class EmpleadoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "puesto")
    private String puesto;

    @Column(name = "salario")
    private BigDecimal salario;

    @Column(name = "fecha_contratacion")
    private LocalDate fecha_contratacion;

}

