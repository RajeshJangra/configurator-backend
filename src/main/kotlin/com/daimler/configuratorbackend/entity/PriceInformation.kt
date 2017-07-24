package com.daimler.configuratorbackend.entity

import java.io.Serializable
import java.util.*

class PriceInformation : Serializable {
    val price: String? = null
    val netPrice: String? = null
    val currency: String? = null
    val taxes: Array<Tax>? = null

    override fun toString(): String {
        return "PriceInformation(price=$price, " +
                "netPrice=$netPrice, " +
                "currency=$currency, " +
                "taxes=${Arrays.toString(taxes)})"
    }
}