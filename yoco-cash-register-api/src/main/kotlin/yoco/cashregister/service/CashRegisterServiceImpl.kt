package za.co.yoco.cashregister.service

import za.co.yoco.cashregister.domain.Cart
import za.co.yoco.cashregister.domain.Item
import za.co.yoco.cashregister.domain.repository.CartRepository
import za.co.yoco.cashregister.domain.service.CashRegisterService

class CashRegisterServiceImpl(
    private val cartRepository: CartRepository
) : CashRegisterService {
    override fun createNewCart(): Cart {
        return cartRepository.createCart()
    }

    override fun addItemToCart(cartId: String, item: Item): Cart {
        val cart = cartRepository.getCart(cartId)
            ?: throw NoSuchElementException("Cart with id '$cartId' was not found")

        val updatedCart = cart.copy(items = cart.items.filterNot { it.id == item.id } + item)
        return cartRepository.saveCart(updatedCart)
    }

    override fun removeItemFromCart(cartId: String, itemId: String): Cart {
        val cart = cartRepository.getCart(cartId)
            ?: throw NoSuchElementException("Cart with id '$cartId' was not found")

        val updatedCart = cart.copy(items = cart.items.filterNot { it.id == itemId })
        return cartRepository.saveCart(updatedCart)
    }
}