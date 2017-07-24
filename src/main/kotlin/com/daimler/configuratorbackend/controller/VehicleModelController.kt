package com.daimler.configuratorbackend.controller

import com.daimler.configuratorbackend.entity.VehicleModel
import com.daimler.configuratorbackend.service.CorPinterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
class VehicleModelController {

    @Autowired
    lateinit var corPinterService: CorPinterService


    @GetMapping("/search")
    fun search(@RequestParam(value = "searchParam") searchParam: String): VehicleModel {
        return corPinterService.getVehicleModelData(searchParam)
    }
}
