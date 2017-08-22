package com.daimler.configurator.service;

import com.daimler.configurator.entity.VehicleModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class CacheServiceTest {

    @InjectMocks
    CacheService service;
    @Mock
    CorPinterService corPinterService;
    @Mock
    RedisService redisService;

    @Test
    public void shouldUpdateCacheWithVehicleData() {
        String searchParam = "SUV";
        VehicleModel vehicleModel = new VehicleModel();

/*        when(corPinterService.)
        Set<VehicleModel> vehicleModels = new HashSet<>();
        when(redisTemplate.executePipelined(any(RedisCallback.class))).thenReturn(null);
        vehicleModels.add(vehicleModel);
        Set<VehicleModel> vehicleModelData = service.search(searchParam);
        assertEquals(vehicleModels, vehicleModelData);*/
    }
}
