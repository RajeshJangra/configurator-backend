package com.daimler.configurator.service;

import com.daimler.configurator.entity.VehicleModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class CorPinterServiceTest {

    @InjectMocks
    CorPinterService service;
    @Mock
    RestTemplate restTemplate;

    @Test
    public void shouldFetchVehicleModelData() {
        VehicleModel vehicleModel = new VehicleModel();
        VehicleModel[] vehicleModels = {vehicleModel};
        service.corPinterUrl = "https://api.corpinter.net/embccs/api/v1/markets/de_DE/dataversion/494bf39d/models";
        when(restTemplate.getForObject(service.corPinterUrl, VehicleModel[].class)).thenReturn(vehicleModels);
        VehicleModel[] vehicleModelData = service.getVehicleModelData();
        assertEquals(vehicleModels, vehicleModelData);
    }
}
