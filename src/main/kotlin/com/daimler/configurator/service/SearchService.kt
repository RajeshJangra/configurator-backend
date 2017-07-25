package com.daimler.configurator.service

import com.daimler.configurator.entity.VehicleModel
import com.daimler.configurator.exception.ModelNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SearchService {

    @Autowired
    lateinit var cacheService: CacheService

    fun searchVehicleModel(param: String): Set<VehicleModel>? {
        return cacheService.search(param) ?: throw ModelNotFoundException(param)
    }
}