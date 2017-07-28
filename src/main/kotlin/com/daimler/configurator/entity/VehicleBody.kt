package com.daimler.configurator.entity

import java.io.Serializable

class VehicleBody : Serializable {
    val bodyId: String? = null
    val bodyName: String? = null
    val links: Links? = null

    override fun toString(): String {
        return "VehicleBody(bodyId=$bodyId, " +
            "bodyName=$bodyName, " +
            "links=$links)"
    }
}