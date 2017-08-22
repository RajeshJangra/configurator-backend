package com.daimler.configurator.service;

import com.daimler.configurator.entity.VehicleModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class SearchServiceTest {

    @InjectMocks
    SearchService service;
    @Mock
    CacheService cacheService;

    @Test
    public void shouldFetchVehicleModelData() {
        String searchParam = "SUV";
        VehicleModel vehicleModel = new VehicleModel();
        Set<VehicleModel> vehicleModels = new HashSet<>();
        vehicleModels.add(vehicleModel);
        when(cacheService.search(searchParam)).thenReturn(vehicleModels);
        Set<VehicleModel> vehicleModelData = service.searchVehicleModel(searchParam);
        assertEquals(vehicleModels, vehicleModelData);
    }
}
