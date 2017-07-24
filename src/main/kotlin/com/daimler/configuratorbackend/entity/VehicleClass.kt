package com.daimler.configuratorbackend.entity

import java.io.Serializable

class VehicleClass : Serializable {
    val classId: String? = null
    val className: String? = null
    val links: Links? = null

    override fun toString(): String {
        return "VehicleClass(classId=$classId, " +
                "className=$className, " +
                "links=$links)"
    }
}