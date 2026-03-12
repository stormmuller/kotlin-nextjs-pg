package za.co.yoco.cashregister.domain.service

import za.co.yoco.cashregister.domain.Cart
import za.co.yoco.cashregister.domain.Item

interface CashRegisterService {
    fun createNewCart(): za.co.yoco.cashregister.domain.Cart
    fun addItemToCart(cartId: String, item: za.co.yoco.cashregister.domain.Item): za.co.yoco.cashregister.domain.Cart
    fun removeItemFromCart(cartId: String, itemId: String): za.co.yoco.cashregister.domain.Cart
}