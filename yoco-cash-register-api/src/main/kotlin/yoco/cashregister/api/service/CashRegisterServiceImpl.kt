package za.co.yoco.cashregister.api.service

import java.util.UUID
import za.co.yoco.cashregister.api.domain.Cart
import za.co.yoco.cashregister.api.domain.Item
import za.co.yoco.cashregister.api.domain.repository.CartRepository
import za.co.yoco.cashregister.api.domain.service.CashRegisterService

class CashRegisterServiceImpl(
    private val cartRepository: CartRepository
) : CashRegisterService {
    override fun createNewCart(): Cart {
        val cart = Cart(id = UUID.randomUUID().toString())
        cartRepository.saveCart(cart)
        return cart
    }

    override fun addItemToCart(cartId: String, item: Item): Cart {
        val cart = cartRepository.getCart(cartId)
            ?: throw NoSuchElementException("Cart with id '$cartId' was not found")

        val updatedCart = cart.copy(items = cart.items + item)
        cartRepository.saveCart(cart)

        return updatedCart
    }

    override fun removeItemFromCart(cartId: String, itemId: String): Cart {
        val cart = cartRepository.getCart(cartId)
            ?: throw NoSuchElementException("Cart with id '$cartId' was not found")

        val itemExists = cart.items.any { it.id == itemId }
        if (!itemExists) {
            throw NoSuchElementException("Item with id '$itemId' was not found in cart '$cartId'")
        }

        val updatedCart = cart.copy(items = cart.items.filterNot { it.id == itemId })
        cartRepository.saveCart(cart)

        return updatedCart
    }
}