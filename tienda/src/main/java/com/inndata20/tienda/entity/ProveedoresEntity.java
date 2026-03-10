package com.inndata20.tienda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "proveedores")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ProveedoresEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "nombre_de_la_empresa")
    private String nombreDeLaEmpresa;
    @Column (name = "contacto")
    private String contacto;
    @Column (name = "telefono")
    private String telefono;
    @Column (name = "correo")
    private String correo;
    @Column(name = "activo",nullable = false)
    private boolean activo = true;
}
