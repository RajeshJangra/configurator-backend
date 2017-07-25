package com.daimler.configuratorbackend.controller

import com.daimler.configuratorbackend.entity.VehicleModel
import com.daimler.configuratorbackend.service.SearchService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController


@RestController(value = "/search")
class SearchController {

    @Autowired
    lateinit var searchService: SearchService

    @GetMapping(value = "/{param}")
    fun search(@PathVariable("param") param: String): Set<VehicleModel>? {
        return searchService.searchVehicleModel(param)
    }
}
