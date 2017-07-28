package com.daimler.configurator.entity

import java.io.Serializable

class Tax : Serializable {
    val id: String? = null
    val amount: String? = null
    val baseAmount: String? = null
    val charge: String? = null
    val rate: String? = null

    override fun toString(): String {
        return "Tax(id=$id, " +
            "amount=$amount, " +
            "baseAmount=$baseAmount, " +
            "charge=$charge, " +
            "rate=$rate)"
    }
}