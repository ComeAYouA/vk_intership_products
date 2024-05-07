package com.example.vkproducts.feature.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkproducts.core.model.ProductsList
import com.example.vkproducts.core.usecase.LoadProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.vkproducts.core.model.Result

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val loadProductsUseCase: LoadProductsUseCase
): ViewModel()   {

    private val productsOnPage = 20

    private val _uiState = MutableStateFlow(
        FeedUiState(
            isLoading = false,
            content = null,
            currentPage = 0,
            error = null
        )
    )

    val uiState = _uiState.asStateFlow()

    init {
        loadProducts()
    }


    fun nextProductsPage() = loadProducts(uiState.value.currentPage * productsOnPage)
    fun reloadProducts() = loadProducts(skip = (uiState.value.currentPage - 1) * productsOnPage)
    fun previousProductsPage() = loadProducts((uiState.value.currentPage - 2 ) * productsOnPage)

    private fun loadProducts(skip: Int = 0){
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            val productsRequest = loadProductsUseCase(
                limit = productsOnPage,
                skip = skip
            )

            updateUiStateWithResult(productsRequest)
        }
    }

    private fun updateUiStateWithResult(result: Result<ProductsList>){
        when(result){
            is Result.Success -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        content = result.data,
                        currentPage = result.data.skip / productsOnPage + 1,
                        error = null
                    )
                }
            }
            is Result.Error ->{
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = result
                    )
                }
            }
        }
    }
}