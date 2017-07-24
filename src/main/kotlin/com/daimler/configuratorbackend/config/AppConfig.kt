package com.daimler.configuratorbackend.config

import com.daimler.configuratorbackend.entity.VehicleModel
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory



@Configuration
class AppConfig {

    @Value("\${redis.host}")
    private val redisHost: String? = null

    @Value("\${redis.port}")
    private val redisPort: Int? = null

    @Bean
    fun redisTemplate(): RedisTemplate<String, VehicleModel> {
        val template = RedisTemplate<String, VehicleModel>()
        template.connectionFactory = jedisConnectionFactory()
        return template
    }

    @Bean
    fun jedisConnectionFactory(): JedisConnectionFactory {
        val jedisConFactory = JedisConnectionFactory()
        jedisConFactory.hostName = redisHost
        jedisConFactory.port = redisPort as Int
        return jedisConFactory
    }

    @Bean
    internal fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}
