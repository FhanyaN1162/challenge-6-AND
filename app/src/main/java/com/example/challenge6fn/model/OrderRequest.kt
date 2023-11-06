package com.example.challenge6fn.model

data class OrderRequest(
    val orders: List<Order>,
    val total: Int?,
    val username: String?
)
