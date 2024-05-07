package com.example.vkproducts.api

import com.example.vkproducts.core.model.ProductsList
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductsApi {

    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int = 20,
        @Query("skip") skip: Int,
    ): ProductsList

}