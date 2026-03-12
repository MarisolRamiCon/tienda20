package com.inndata20.tienda.service.implementacion;

import com.inndata20.tienda.feign.ForaneoEntity;
import com.inndata20.tienda.feign.ForaneoClient;
import com.inndata20.tienda.service.IForaneoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class ForaneoService implements IForaneoService {

    private final ForaneoClient foraneoClient;

    public ForaneoService(ForaneoClient foraneoClient) {
        this.foraneoClient = foraneoClient;
    }

    @Override
    public List<ForaneoEntity> readAll(){
        return foraneoClient.readAll();
    }

}
