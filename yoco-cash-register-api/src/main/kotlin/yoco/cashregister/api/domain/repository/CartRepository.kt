package za.co.yoco.cashregister.api.domain.repository

import za.co.yoco.cashregister.api.domain.Cart

interface CartRepository {
    fun getCart(id: String): Cart?
    fun saveCart(cart: Cart)
}