package com.example.vkproducts.feature.feed

import com.example.vkproducts.core.model.ProductsList
import com.example.vkproducts.core.model.Result

data class FeedUiState(
    var isLoading: Boolean,
    var content: ProductsList? = null,
    var currentPage: Int,
    var error: Result.Error<ProductsList>?
)