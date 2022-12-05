package com.safefleet.customgatewayfilter.service.implementation;

import com.safefleet.customgatewayfilter.service.IUnitUriService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Slf4j
@Service
public class UnitUriServiceImpl implements IUnitUriService {

    private static Random rnd = new Random();
    String[] unitRoutes = {"http://localhost:8447/units", "http://localhost:8446/users"};

    @Override
    public String getUrl() {
        return unitRoutes[rnd.nextInt(unitRoutes.length)];
    }
}
