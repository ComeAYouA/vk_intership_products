package com.example.vkproducts.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("thumbnail")
    val thumbnail: String,
    @SerialName("price")
    val price: Int
)
