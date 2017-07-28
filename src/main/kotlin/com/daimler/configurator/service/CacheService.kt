package com.daimler.configurator.service

import com.daimler.configurator.constant.AppConstants.CACHE_BUILT_SUCCESS
import com.daimler.configurator.constant.AppConstants.CACHE_CLEARED_SUCCESSFULLY
import com.daimler.configurator.constant.AppConstants.WILDCARD
import com.daimler.configurator.constant.CacheKey.MODEL_ID
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
                addToPrimaryCache(connection, it, it.modelId?.toByteArray(), MODEL_ID.toString().toByteArray())
                addToIndirectionCache(connection, it.modelId?.toByteArray(), it.name?.toByteArray())
                addToIndirectionCache(connection, it.modelId?.toByteArray(), it.vehicleClass?.className?.toByteArray())
                addToIndirectionCache(connection, it.modelId?.toByteArray(), it.vehicleBody?.bodyName?.toByteArray())
            }
            null
        })
        return CACHE_BUILT_SUCCESS
    }

    private fun addToPrimaryCache(connection: RedisConnection, it: VehicleModel, key: ByteArray?, collectionName: ByteArray) {
        connection.hashCommands().hSet(collectionName, key, Jackson2JsonRedisSerializer(VehicleModel::class.java).serialize(it))
        connection.expire(key, timeToLive)
    }

    private fun addToIndirectionCache(connection: RedisConnection, modelId: ByteArray?, key: ByteArray?) {
        connection.setCommands().sAdd(key, modelId)
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
            val entries = getModelIdsFromIndirectionCache(key, redisConnection)
            val elements = getVehicleModelsFromIndirectionCache(entries, redisConnection)
            result.addAll(elements)
        }
        return result
    }

    private fun getFromCache(key: String, collectionName: ByteArray, redisConnection: RedisConnection): Cursor<MutableMap.MutableEntry<ByteArray, ByteArray>>? {
        return redisConnection.hScan(collectionName, scanOptions(key))
    }

    private fun getModelIdsFromIndirectionCache(key: String, redisConnection: RedisConnection): Cursor<ByteArray>? {
        return redisConnection.sScan(key.toByteArray(), scanOptionsIndirection())
    }

    private fun scanOptionsIndirection(): ScanOptions? {
        return ScanOptions.scanOptions().match(WILDCARD).build()
    }

    private fun scanOptions(key: String): ScanOptions? {
        return ScanOptions.scanOptions().match(getPattern(key)).count(1).build()
    }

    private fun getVehicleModelsFromIndirectionCache(entries: Cursor<ByteArray>?, redisConnection: RedisConnection): HashSet<VehicleModel> {
        val result = HashSet<VehicleModel>()
        if (entries != null)
            while (entries.hasNext()) {
                val element = redisConnection.hashCommands().hGet(MODEL_ID.toString().toByteArray(), entries.next())
                val vehicleModel = Jackson2JsonRedisSerializer(VehicleModel::class.java).deserialize(element)
                result.add(vehicleModel)
            }
        return result
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
