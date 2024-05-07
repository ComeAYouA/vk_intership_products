package com.example.vkproducts.api.di

import com.example.vkproducts.api.ProductsApi
import com.example.vkproducts.api.ProductsApiImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

@InstallIn(SingletonComponent::class)
@Module
interface NetworkModule{

    @Binds
    fun bindsProductsApiImpl_to_ProductsApi(input: ProductsApiImpl): ProductsApi

    companion object{
        private val json = Json {
            coerceInputValues = true
            ignoreUnknownKeys = true
        }

        @Provides
        fun providesRetrofit(): Retrofit{
            return Retrofit
                .Builder()
                .baseUrl("https://dummyjson.com/")
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()
        }
    }
}