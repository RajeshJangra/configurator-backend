package com.daimler.configuratorbackend.service

import com.daimler.configuratorbackend.entity.VehicleModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class CacheService {

    @Autowired
    internal var redisTemplate: RedisTemplate<String, VehicleModel>? = null

    fun add(key: String, value: VehicleModel) {
        redisTemplate?.opsForValue()?.set(key, value)
    }

    fun getValue(key: String): VehicleModel? {
        return redisTemplate?.opsForValue()?.get(key)
    }

    fun delete(key: String) {
        redisTemplate?.opsForValue()?.operations?.delete(key)
    }
}
