package com.daimler.configuratorbackend.service

import com.daimler.configuratorbackend.constant.AppConstants.CACHE_BUILT_SUCCESS
import com.daimler.configuratorbackend.constant.AppConstants.CACHE_CLEARED_SUCCESSFULLY
import com.daimler.configuratorbackend.constant.AppConstants.WILDCARD
import com.daimler.configuratorbackend.constant.CacheKey.*
import com.daimler.configuratorbackend.entity.VehicleModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.connection.RedisConnection
import org.springframework.data.redis.core.Cursor
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ScanOptions
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


    fun addToCache(vehicleModels: Array<VehicleModel>) {
        redisTemplate.executePipelined({ connection ->
            Arrays.stream(vehicleModels).forEach {
                addByModelId(connection, it)
                addByName(connection, it)
                addByClassName(connection, it)
                addByBodyName(connection, it)
            }
            null
        })
    }

    private fun addByModelId(connection: RedisConnection, it: VehicleModel) {
        connection.hashCommands().hSet(MODEL_ID.toString().toByteArray(), it.modelId?.toByteArray(), Jackson2JsonRedisSerializer(VehicleModel::class.java).serialize(it))
        connection.expire(it.modelId?.toByteArray(), timeToLive)
    }

    private fun addByName(connection: RedisConnection, it: VehicleModel) {
        connection.hashCommands().hSet(NAME.toString().toByteArray(), it.name?.toByteArray(), Jackson2JsonRedisSerializer(VehicleModel::class.java).serialize(it))
        connection.expire(it.name?.toByteArray(), timeToLive)
    }

    private fun addByClassName(connection: RedisConnection, it: VehicleModel) {
        connection.hashCommands().hSet(CLASS_NAME.toString().toByteArray(), it.vehicleClass?.className?.toByteArray(), Jackson2JsonRedisSerializer(VehicleModel::class.java).serialize(it))
        connection.expire(it.vehicleClass?.className?.toByteArray(), timeToLive)
    }

    private fun addByBodyName(connection: RedisConnection, it: VehicleModel) {
        connection.hashCommands().hSet(BODY_NAME.toString().toByteArray(), it.vehicleBody?.bodyName?.toByteArray(), Jackson2JsonRedisSerializer(VehicleModel::class.java).serialize(it))
        connection.expire(it.vehicleBody?.bodyName?.toByteArray(), timeToLive)
    }

    fun search(key: String): Set<VehicleModel>? {
        val result = HashSet<VehicleModel>()
        redisTemplate.execute { redisConnection ->
            result.addAll(addEntriesToSet(getByModelId(key, redisConnection)))
            result.addAll(addEntriesToSet(getByName(key, redisConnection)))
            result.addAll(addEntriesToSet(getByClassName(key, redisConnection)))
            result.addAll(addEntriesToSet(getByBodyName(key, redisConnection)))
        }
        return result
    }

    private fun getByModelId(key: String, redisConnection: RedisConnection): Cursor<MutableMap.MutableEntry<ByteArray, ByteArray>>? {
        return redisConnection.hScan(MODEL_ID.toString().toByteArray(), scanOptions(key))
    }

    private fun getByName(key: String, redisConnection: RedisConnection): Cursor<MutableMap.MutableEntry<ByteArray, ByteArray>>? {
        return redisConnection.hScan(NAME.toString().toByteArray(), scanOptions(key))
    }

    private fun getByClassName(key: String, redisConnection: RedisConnection): Cursor<MutableMap.MutableEntry<ByteArray, ByteArray>>? {
        return redisConnection.hScan(CLASS_NAME.toString().toByteArray(), scanOptions(key))
    }

    private fun getByBodyName(key: String, redisConnection: RedisConnection): Cursor<MutableMap.MutableEntry<ByteArray, ByteArray>>? {
        return redisConnection.hScan(BODY_NAME.toString().toByteArray(), scanOptions(key))
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

    fun clearCache(): String {
        redisTemplate.execute { it.serverCommands().flushAll() }
        return CACHE_CLEARED_SUCCESSFULLY
    }

    fun buildCache(): String {
        addToCache(corPinterService.getVehicleModelData())
        return CACHE_BUILT_SUCCESS
    }
}
