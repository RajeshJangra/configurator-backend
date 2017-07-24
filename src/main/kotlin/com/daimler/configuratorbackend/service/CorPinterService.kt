package com.daimler.configuratorbackend.service

import com.daimler.configuratorbackend.entity.VehicleModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class CorPinterService {

    @Value("\${cor.pinter.url}")
    private val corPinterUrl: String? = null

    @Autowired
    private val restTemplate: RestTemplate? = null

    @Autowired
    private val cacheService: CacheService? = null

    fun getVehicleModelData(modelId: String): VehicleModel {
        var vehicleModel = cacheService?.getValue(modelId)
        if (vehicleModel == null) {
            val vehicleModels = restTemplate!!.getForObject(corPinterUrl, Array<VehicleModel>::class.java)
            vehicleModels.forEach {
                cacheService?.add(it.modelId!!, it)
                if (it.modelId == modelId) {
                    vehicleModel = it
                }
            }
        }
        return vehicleModel as VehicleModel
    }
}
