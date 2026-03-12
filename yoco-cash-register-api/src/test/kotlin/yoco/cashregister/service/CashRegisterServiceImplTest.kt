package za.co.yoco.cashregister.service

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import za.co.yoco.cashregister.api.domain.Item
import za.co.yoco.cashregister.api.domain.Money

class CashRegisterServiceImplTest {

    @Test
    fun `createNewCart generates an id`() {
        val service = CashRegisterServiceImpl()

        val cart = service.createNewCart()

        assertTrue(cart.id.isNotBlank())
    }

    @Test
    fun `createNewCart generates unique ids`() {
        val service = CashRegisterServiceImpl()

        val firstCart = service.createNewCart()
        val secondCart = service.createNewCart()

        assertNotEquals(firstCart.id, secondCart.id)
    }

    @Test
    fun `new cart starts empty`() {
        val service = CashRegisterServiceImpl()

        val cart = service.createNewCart()

        assertTrue(cart.items.isEmpty())
    }

    @Test
    fun `addItemToCart throws when cart does not exist`() {
        val service = CashRegisterServiceImpl()
        val item = Item(id = "item-1", amount = Money(1234))

        val exception = assertFailsWith<NoSuchElementException> {
            service.addItemToCart("missing-cart", item)
        }

        assertEquals("Cart with id 'missing-cart' was not found", exception.message)
    }

    @Test
    fun `removeItemFromCart throws when cart does not exist`() {
        val service = CashRegisterServiceImpl()

        val exception = assertFailsWith<NoSuchElementException> {
            service.removeItemFromCart("missing-cart", "item-1")
        }

        assertEquals("Cart with id 'missing-cart' was not found", exception.message)
    }

    @Test
    fun `removeItemFromCart throws when item does not exist in cart`() {
        val service = CashRegisterServiceImpl()
        val cart = service.createNewCart()

        val exception = assertFailsWith<NoSuchElementException> {
            service.removeItemFromCart(cart.id, "missing-item")
        }

        assertEquals("Item with id 'missing-item' was not found in cart '${cart.id}'", exception.message)
    }
}