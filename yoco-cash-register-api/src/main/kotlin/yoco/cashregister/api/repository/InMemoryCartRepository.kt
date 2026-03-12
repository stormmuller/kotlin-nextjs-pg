package za.co.yoco.cashregister.api.repository

import za.co.yoco.cashregister.api.domain.Cart
import za.co.yoco.cashregister.api.domain.repository.CartRepository

class InMemoryCartRepository : CartRepository {
    private val carts = mutableMapOf<String, Cart>()

    override fun getCart(id: String): Cart? = carts[id]

    override fun saveCart(cart: Cart) {
        carts[cart.id] = cart
    }
}