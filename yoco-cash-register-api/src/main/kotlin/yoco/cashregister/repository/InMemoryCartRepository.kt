package za.co.yoco.cashregister.repository

import za.co.yoco.cashregister.domain.Cart
import za.co.yoco.cashregister.domain.repository.CartRepository
import java.util.UUID

class InMemoryCartRepository : CartRepository {
    private val carts = mutableMapOf<String, Cart>()

    override fun getCart(id: String): Cart? = carts[id]

    override fun createCart(): Cart {
        val cart = Cart(id = UUID.randomUUID().toString())
        carts[cart.id] = cart
        return cart
    }

    override fun saveCart(cart: Cart): Cart {
        val cartWithIds = cart.copy(items = cart.items.map { it.withGeneratedId() })
        carts[cartWithIds.id] = cartWithIds
        return cartWithIds
    }
}