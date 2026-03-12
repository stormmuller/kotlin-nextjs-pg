package za.co.yoco.cashregister.api.service

import java.util.UUID
import za.co.yoco.cashregister.api.domain.Cart
import za.co.yoco.cashregister.api.domain.Item
import za.co.yoco.cashregister.api.domain.service.CashRegisterService

class CashRegisterServiceImpl : CashRegisterService {
    private val carts = mutableMapOf<String, Cart>()

    override fun createNewCart(): Cart {
        val cart = Cart(id = UUID.randomUUID().toString())
        carts[cart.id] = cart
        return cart
    }

    override fun addItemToCart(cartId: String, item: Item): Cart {
        val cart = carts[cartId]
            ?: throw NoSuchElementException("Cart with id '$cartId' was not found")

        val updatedCart = cart.copy(items = cart.items + item)
        carts[cartId] = updatedCart

        return updatedCart
    }

    override fun removeItemFromCart(cartId: String, itemId: String): Cart {
        val cart = carts[cartId]
            ?: throw NoSuchElementException("Cart with id '$cartId' was not found")

        val itemExists = cart.items.any { it.id == itemId }
        if (!itemExists) {
            throw NoSuchElementException("Item with id '$itemId' was not found in cart '$cartId'")
        }

        val updatedCart = cart.copy(items = cart.items.filterNot { it.id == itemId })
        carts[cartId] = updatedCart

        return updatedCart
    }
}