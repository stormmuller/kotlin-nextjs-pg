package za.co.yoco.cashregister.domain.repository

import za.co.yoco.cashregister.domain.Cart

interface CartRepository {
    fun getCart(id: String): Cart?
    fun createCart(): Cart
    fun saveCart(cart: Cart): Cart
}