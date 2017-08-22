package com.daimler.configurator.service

import com.daimler.configurator.constant.AppConstants
import com.daimler.configurator.constant.AppConstants.MODEL_ID
import com.daimler.configurator.constant.AppConstants.VEHICLE_MODELS
import com.daimler.configurator.constant.AppConstants.WILDCARD
import com.daimler.configurator.entity.VehicleModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.connection.RedisConnection
import org.springframework.data.redis.core.*
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.stereotype.Service
import java.util.*

@Service
class RedisService {

    @Autowired
    lateinit var redisTemplate: RedisTemplate<String, VehicleModel>

    @Value("\${redis.timeToLive}")
    private val timeToLive: Long = 86400

    fun buildCache(vehicleModels: Array<VehicleModel>) {
        redisTemplate.executePipelined(
                { connection -> Arrays
                        .stream(vehicleModels)
                        .map { vehicleModel -> addToCache(vehicleModel, connection) }
                    null
                }
        )
    }

    private fun addToCache(vehicleModel: VehicleModel, connection: RedisConnection) {
        val modelId = vehicleModel.modelId
        addToPrimaryCache(VEHICLE_MODELS, modelId, vehicleModel, connection)
        addToIndirectionCache(vehicleModel.name, modelId, connection)
        addToIndirectionCache(vehicleModel.vehicleClass?.className, modelId, connection)
        addToIndirectionCache(vehicleModel.vehicleBody?.bodyName, modelId, connection)
    }

    private fun addToPrimaryCache(collectionName: String, key: String?, it: VehicleModel, connection: RedisConnection) {
        connection.hashCommands().hSet(collectionName.toByteArray(), key?.toByteArray(), Jackson2JsonRedisSerializer(VehicleModel::class.java).serialize(it))
        connection.expire(key?.toByteArray(), timeToLive)
    }

    private fun addToIndirectionCache(key: String?, modelId: String?, connection: RedisConnection) {
        connection.setCommands().sAdd(key?.toByteArray(), modelId?.toByteArray())
        connection.expire(key?.toByteArray(), timeToLive)
    }

    fun fetchByModelId(key: String): Cursor<MutableMap.MutableEntry<ByteArray, ByteArray>>? {
        var cursor: Cursor<MutableMap.MutableEntry<ByteArray, ByteArray>>? = null
        redisTemplate.executePipelined({ redisConnection ->
            cursor = redisConnection.hScan(MODEL_ID.toByteArray(), scanOptions(WILDCARD + key + WILDCARD))
            null
        })
        return cursor
    }

    fun clearCache(): String {
        redisTemplate.execute { it.serverCommands().flushAll() }
        return AppConstants.CACHE_CLEARED_SUCCESSFULLY
    }

    fun fetchBySecondaryParams(key: String): HashSet<VehicleModel> {
        var vehicleModels = HashSet<VehicleModel>()
        redisTemplate.executePipelined({ redisConnection ->
            val modelIds = redisConnection.sScan(key.toByteArray(), scanOptions(WILDCARD))
            vehicleModels = getVehicleModelsFromIndirectionCache(modelIds, redisConnection)
            null
        })
        return vehicleModels
    }

    private fun scanOptions(pattern: String): ScanOptions? {
        return ScanOptions.scanOptions().match(pattern).build()
    }

    private fun getVehicleModelsFromIndirectionCache(entries: Cursor<ByteArray>?, redisConnection: RedisConnection): HashSet<VehicleModel> {
        val result = HashSet<VehicleModel>()
        if (entries != null)
            while (entries.hasNext()) {
                val element = redisConnection.hashCommands().hGet(MODEL_ID.toByteArray(), entries.next())
                val vehicleModel = Jackson2JsonRedisSerializer(VehicleModel::class.java).deserialize(element)
                result.add(vehicleModel)
            }
        return result
    }
}
