package za.co.yoco.cashregister.api.domain.service

import za.co.yoco.cashregister.api.domain.Cart
import za.co.yoco.cashregister.api.domain.Item

interface CashRegisterService {
    fun createNewCart(): Cart
    fun addItemToCart(cartId: String, item: Item): Cart
    fun removeItemFromCart(cartId: String, itemId: String): Cart
}