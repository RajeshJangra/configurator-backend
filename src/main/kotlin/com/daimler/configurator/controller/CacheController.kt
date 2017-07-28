package com.daimler.configurator.controller

import com.daimler.configurator.service.CacheService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

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
