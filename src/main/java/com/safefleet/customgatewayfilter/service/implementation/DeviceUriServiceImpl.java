package com.safefleet.customgatewayfilter.service.implementation;

import com.safefleet.customgatewayfilter.service.IDeviceUriService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class DeviceUriServiceImpl implements IDeviceUriService {

    private static Random rnd = new Random();
    String[] deviceRoutes = {"https://google.com", "https://youtube.com"};

    @Override
    public String getUrl() {
        var url = deviceRoutes[rnd.nextInt(deviceRoutes.length)];
        return url;
    }
}
