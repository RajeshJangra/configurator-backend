package com.daimler.configuratorbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class Cache {

    @Autowired
    RedisTemplate redisTemplate;

/*    private void search() {
        redisTemplate.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                ScanOptions options = ScanOptions.scanOptions().match("205").count(1).build();
                Cursor<Map.Entry<byte[], byte[]>> entries = redisConnection.hScan("modelId".getBytes(), options);
                List<String> result = new ArrayList<>();

                if (entries != null)
                    while (entries.hasNext()) {
                        result.add(new String(entries.next().getValue()));
                    }
                return result;
            }
        });
    }

    private void addToCache(VehicleModel[] vehicleModels) {

        redisTemplate.executePipelined(
                new RedisCallback<Object>() {
                    public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                        Arrays.stream(vehicleModels).forEach(vehicleModel -> {
                            ((StringRedisConnection) redisConnection).stringCommands().set(vehicleModel.getModelId().getBytes(), vehicleModel.toString().getBytes());
                        });
                        return null;
                    }
                }
        );
    }*/
}
