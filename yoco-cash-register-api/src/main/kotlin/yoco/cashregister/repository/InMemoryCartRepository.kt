package za.co.yoco.cashregister.repository

import za.co.yoco.cashregister.domain.Cart
import za.co.yoco.cashregister.domain.repository.CartRepository

class InMemoryCartRepository : za.co.yoco.cashregister.domain.repository.CartRepository {
    private val carts = mutableMapOf<String, za.co.yoco.cashregister.domain.Cart>()

    override fun getCart(id: String): za.co.yoco.cashregister.domain.Cart? = carts[id]

    override fun saveCart(cart: za.co.yoco.cashregister.domain.Cart) {
        carts[cart.id] = cart
    }
}