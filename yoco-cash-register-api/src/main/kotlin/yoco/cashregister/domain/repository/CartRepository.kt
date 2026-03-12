package za.co.yoco.cashregister.domain.repository

import za.co.yoco.cashregister.domain.Cart

interface CartRepository {
    fun getCart(id: String): za.co.yoco.cashregister.domain.Cart?
    fun saveCart(cart: za.co.yoco.cashregister.domain.Cart)
}