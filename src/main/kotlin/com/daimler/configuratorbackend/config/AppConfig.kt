package com.daimler.configuratorbackend.config

import com.daimler.configuratorbackend.entity.VehicleModel
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.web.client.RestTemplate


@Configuration
class AppConfig {

    @Value("\${redis.host}")
    private val redisHost: String = "localhost"

    @Value("\${redis.port}")
    private val redisPort: Int = 6379

    @Bean
    fun redisTemplate(): RedisTemplate<String, VehicleModel> {
        val template = RedisTemplate<String, VehicleModel>()
        template.connectionFactory = jedisConnectionFactory()
        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = Jackson2JsonRedisSerializer(VehicleModel::class.java)
        template.afterPropertiesSet()
        return template
    }

/*    @Bean
    fun stringRedisTemplate(jedisConnectionFactory: JedisConnectionFactory): StringRedisTemplate {
        val template = StringRedisTemplate()
        template.connectionFactory = jedisConnectionFactory
        return template
    }*/

    @Bean
    fun jedisConnectionFactory(): JedisConnectionFactory {
        val connectionFactory = JedisConnectionFactory()
        connectionFactory.hostName = redisHost
        connectionFactory.port = redisPort
        connectionFactory.usePool = true
        return connectionFactory
    }

    @Bean
    internal fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}
