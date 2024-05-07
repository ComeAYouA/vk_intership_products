package com.example.vkproducts.api

import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Inject

class ProductsApiImpl @Inject constructor(
    private val retrofit: Retrofit
) : ProductsApi {
    private val api = retrofit.create(ProductsApi::class.java)

    override suspend fun getProducts(limit: Int, skip: Int) =
        api.getProducts(limit, skip)
}