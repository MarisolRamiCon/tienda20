package com.inndata20.tienda.service;

import com.inndata20.tienda.feign.ForaneoEntity;

import java.util.List;

public interface IForaneoService {
    List<ForaneoEntity> readAll();
}
