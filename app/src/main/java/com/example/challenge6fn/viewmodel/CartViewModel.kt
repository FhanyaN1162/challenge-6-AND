package com.example.challenge6fn.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.challenge6fn.api.ApiClient
import com.example.challenge6fn.items.CartItem
import com.example.challenge6fn.model.OrderRequest
import com.example.challenge6fn.model.OrderResponse
import com.example.challenge6fn.repository.CartRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartViewModel(private val repository: CartRepository) : ViewModel() {

    val allCartItems: LiveData<List<CartItem>> = repository.allCartItems

    private val orderResult = MutableLiveData<OrderResponse>()
    fun getOrderResult():LiveData<OrderResponse> = orderResult

    fun insertCartItem(cartItem: CartItem) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val cart = repository.getCartByFoodName(cartItem.foodName)
                if(cart==null){
                    repository.insertCartItem(cartItem)
                }else{
                    cart.quantity+=cartItem.quantity
                    repository.updateCartItem(cart)
                }
            }
        }
    }
    fun updateCartItem(cartItem: CartItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCartItem(cartItem)
        }
    }

    fun deleteCartItem(cartItem: CartItem) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteCartItem(cartItem)
            }
        }
    }
    fun deleteAllCartItems() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteAllCartItems()
            }
        }
    }

    fun order(orderRequest: OrderRequest){
        ApiClient.instance
            .order(orderRequest)
            .enqueue(object : Callback<OrderResponse> {
                override fun onResponse(
                    call: Call<OrderResponse>,
                    response: Response<OrderResponse>
                ) {
                    if (response.isSuccessful){
                        orderResult.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                    t.message?.let {
                        Log.d("Failure", it)
                    }
                }
            })
    }

}
