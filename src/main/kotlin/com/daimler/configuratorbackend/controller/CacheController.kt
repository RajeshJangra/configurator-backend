package com.daimler.configuratorbackend.controller

import com.daimler.configuratorbackend.service.CacheService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController


@RestController(value = "/cache")
class CacheController {

    @Autowired
    lateinit var cacheService: CacheService


    @PutMapping
    fun buildCache(): String? {
        return cacheService.buildCache()
    }

    @DeleteMapping
    fun clearCache() = cacheService.clearCache()
}
