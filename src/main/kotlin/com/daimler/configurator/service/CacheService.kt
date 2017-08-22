package com.daimler.configurator.service

import com.daimler.configurator.constant.AppConstants.CACHE_BUILT_SUCCESS
import com.daimler.configurator.entity.VehicleModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.Cursor
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.stereotype.Service
import java.util.*

@Service
class CacheService {

    @Autowired
    lateinit var corPinterService: CorPinterService
    @Autowired
    lateinit var redisService: RedisService

    fun buildCache(): String {
        val vehicleModels = corPinterService.getVehicleModelData()
        redisService.buildCache(vehicleModels)
        return CACHE_BUILT_SUCCESS
    }

    fun clearCache(): String = redisService.clearCache()

    fun search(key: String): Set<VehicleModel>? {
        val result = HashSet<VehicleModel>()
        result.addAll(getByModelId(key))
        result.addAll(getBySecondaryParams(key))
        return result
    }

    private fun getByModelId(key: String): HashSet<VehicleModel> =
            getVehicleModelsFromCursor(redisService.fetchByModelId(key))

    private fun getVehicleModelsFromCursor(entries: Cursor<MutableMap.MutableEntry<ByteArray, ByteArray>>?): HashSet<VehicleModel> {
        val result = HashSet<VehicleModel>()
        if (entries != null)
            while (entries.hasNext()) {
                result.add(Jackson2JsonRedisSerializer(VehicleModel::class.java).deserialize(entries.next().value))
            }
        return result
    }

    private fun getBySecondaryParams(key: String): HashSet<VehicleModel> = redisService.fetchBySecondaryParams(key)

}
