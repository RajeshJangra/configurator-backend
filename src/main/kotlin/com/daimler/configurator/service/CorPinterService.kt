package com.daimler.configurator.service

import com.daimler.configurator.entity.VehicleModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class CorPinterService {

    @Value("\${cor.pinter.url}")
    lateinit var corPinterUrl: String

    @Autowired
    lateinit var restTemplate: RestTemplate

    fun getVehicleModelData(): Array<VehicleModel> {
        return restTemplate.getForObject(corPinterUrl, Array<VehicleModel>::class.java)
    }
}
