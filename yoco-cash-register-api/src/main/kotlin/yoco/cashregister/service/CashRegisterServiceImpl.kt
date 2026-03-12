package za.co.yoco.cashregister.service

import java.util.UUID
import za.co.yoco.cashregister.domain.Cart
import za.co.yoco.cashregister.domain.Item
import za.co.yoco.cashregister.domain.repository.CartRepository
import za.co.yoco.cashregister.domain.service.CashRegisterService

class CashRegisterServiceImpl(
    private val cartRepository: za.co.yoco.cashregister.domain.repository.CartRepository
) : za.co.yoco.cashregister.domain.service.CashRegisterService {
    override fun createNewCart(): za.co.yoco.cashregister.domain.Cart {
        val cart = _root_ide_package_.za.co.yoco.cashregister.domain.Cart(id = UUID.randomUUID().toString())
        cartRepository.saveCart(cart)
        return cart
    }

    override fun addItemToCart(cartId: String, item: za.co.yoco.cashregister.domain.Item): za.co.yoco.cashregister.domain.Cart {
        val cart = cartRepository.getCart(cartId)
            ?: throw NoSuchElementException("Cart with id '$cartId' was not found")

        val updatedCart = cart.copy(items = cart.items + item)
        cartRepository.saveCart(cart)

        return updatedCart
    }

    override fun removeItemFromCart(cartId: String, itemId: String): za.co.yoco.cashregister.domain.Cart {
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