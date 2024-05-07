package com.example.vkproducts.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductsList(
    @SerialName("products")
    val products: List<Product>,
    @SerialName("total")
    val total: Int,
    @SerialName("skip")
    val skip: Int,
    @SerialName("limit")
    val limit: Int
)
