package com.inndata20.tienda.repository;

import com.inndata20.tienda.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ClienteRepository extends JpaRepository<ClienteEntity,Integer> {
    @Query(value = "SELECT * FROM clientes WHERE UPPER(nombre) LIKE UPPER(CONCAT('%', ?1, '%'))", nativeQuery = true)
    public List<ClienteEntity> findByNombre(String busqueda);
    public List<ClienteEntity> findByActivoNotNull();
}
