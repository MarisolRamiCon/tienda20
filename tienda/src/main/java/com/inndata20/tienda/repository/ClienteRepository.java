package com.inndata20.tienda.repository;

import com.inndata20.tienda.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

@Repository

public interface ClienteRepository extends JpaRepository<ClienteEntity,Integer> {
}
