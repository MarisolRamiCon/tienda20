package com.inndata20.tienda.repository;

import com.inndata20.tienda.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<ClienteEntity,Integer> {
}
