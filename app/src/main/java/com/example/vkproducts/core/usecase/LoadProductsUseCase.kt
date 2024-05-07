package com.example.vkproducts.core.usecase

import android.util.Log
import com.example.vkproducts.api.ProductsApi
import com.example.vkproducts.core.model.ProductsList
import java.lang.Exception
import javax.inject.Inject
import com.example.vkproducts.core.model.Result
import com.example.vkproducts.core.utils.NetworkExceptionConverter.convertToString

class LoadProductsUseCase @Inject constructor(
    private val productsApi: ProductsApi
){
    suspend operator fun invoke(limit: Int, skip: Int): Result<ProductsList> {
        return try {
            val products = productsApi.getProducts(
                limit = limit,
                skip = skip
            )

            Result.Success(products)
        } catch(e: Exception){
            Result.Error(
                message = e.convertToString()
            )
        }
    }
}