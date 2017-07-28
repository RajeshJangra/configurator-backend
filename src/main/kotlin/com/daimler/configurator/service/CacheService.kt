package com.daimler.configurator.service

import com.daimler.configurator.constant.AppConstants.CACHE_BUILT_SUCCESS
import com.daimler.configurator.constant.AppConstants.CACHE_CLEARED_SUCCESSFULLY
import com.daimler.configurator.constant.AppConstants.WILDCARD
import com.daimler.configurator.constant.CacheKey.*
import com.daimler.configurator.entity.VehicleModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.connection.RedisConnection
import org.springframework.data.redis.core.*
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.stereotype.Service
import java.util.*

@Service
class CacheService {

    @Autowired
    lateinit var redisTemplate: RedisTemplate<String, VehicleModel>
    @Autowired
    lateinit var corPinterService: CorPinterService

    @Value("\${redis.timeToLive}")
    private val timeToLive: Long = 86400

    fun buildCache(): String {
        val vehicleModels = corPinterService.getVehicleModelData()
        redisTemplate.executePipelined({ connection ->
            Arrays.stream(vehicleModels).forEach {
                addToCache(connection, it, it.modelId?.toByteArray(), MODEL_ID.toString().toByteArray())
                addToCache(connection, it, it.name?.toByteArray(), NAME.toString().toByteArray())
                addToCache(connection, it, it.vehicleClass?.className?.toByteArray(), CLASS_NAME.toString().toByteArray())
                addToCache(connection, it, it.vehicleBody?.bodyName?.toByteArray(), BODY_NAME.toString().toByteArray())
            }
            null
        })
        return CACHE_BUILT_SUCCESS
    }

    private fun addToCache(connection: RedisConnection, it: VehicleModel, key: ByteArray?, collectionName: ByteArray) {
        connection.hashCommands().hSet(collectionName, key, Jackson2JsonRedisSerializer(VehicleModel::class.java).serialize(it))
        connection.expire(key, timeToLive)
    }

    fun clearCache(): String {
        redisTemplate.execute { it.serverCommands().flushAll() }
        return CACHE_CLEARED_SUCCESSFULLY
    }

    fun search(key: String): Set<VehicleModel>? {
        val result = HashSet<VehicleModel>()
        redisTemplate.execute { redisConnection ->
            result.addAll(addEntriesToSet(getFromCache(key, MODEL_ID.toString().toByteArray(), redisConnection)))
            result.addAll(addEntriesToSet(getFromCache(key, NAME.toString().toByteArray(), redisConnection)))
            result.addAll(addEntriesToSet(getFromCache(key, CLASS_NAME.toString().toByteArray(), redisConnection)))
            result.addAll(addEntriesToSet(getFromCache(key, BODY_NAME.toString().toByteArray(), redisConnection)))
        }
        return result
    }

    private fun getFromCache(key: String, collectionName: ByteArray, redisConnection: RedisConnection): Cursor<MutableMap.MutableEntry<ByteArray, ByteArray>>? {
        return redisConnection.hScan(collectionName, scanOptions(key))
    }

    private fun scanOptions(key: String): ScanOptions? {
        return ScanOptions.scanOptions().match(getPattern(key)).count(1).build()
    }

    private fun addEntriesToSet(entries: Cursor<MutableMap.MutableEntry<ByteArray, ByteArray>>?): HashSet<VehicleModel> {
        val result = HashSet<VehicleModel>()
        if (entries != null)
            while (entries.hasNext()) {
                result.add(Jackson2JsonRedisSerializer(VehicleModel::class.java).deserialize(entries.next().value))
            }
        return result
    }

    private fun getPattern(key: String) = WILDCARD + key + WILDCARD
}
