package com.daimler.configurator.entity

import java.io.Serializable

class VehicleModel : Serializable {
    val modelId: String? = null
    val name: String? = null
    val shortName: String? = null
    val baumuster: String? = null
    val nationalSalesType: String? = null
    val vehicleClass: VehicleClass? = null
    val vehicleBody: VehicleBody? = null
    val priceInformation: PriceInformation? = null
    val productGroup: String? = null
    val dataSupply: String? = null
    val links: Links? = null

    override fun toString(): String {
        return "VehicleModel(modelId=$modelId, " +
                "name=$name, " +
                "shortName=$shortName, " +
                "baumuster=$baumuster, " +
                "nationalSalesType=$nationalSalesType, " +
                "vehicleClass=$vehicleClass, " +
                "vehicleBody=$vehicleBody, " +
                "priceInformation=$priceInformation, " +
                "productGroup=$productGroup, " +
                "dataSupply=$dataSupply, " +
                "links=$links)"
    }
}
