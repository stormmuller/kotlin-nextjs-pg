package za.co.yoco.cashregister.service

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import za.co.yoco.cashregister.domain.Item
import za.co.yoco.cashregister.domain.Money
import za.co.yoco.cashregister.repository.InMemoryCartRepository

class CashRegisterServiceImplTest {

    private fun createService() = CashRegisterServiceImpl(InMemoryCartRepository())

    @Test
    fun `createNewCart generates an id`() {
        val service = createService()

        val cart = service.createNewCart()

        assertTrue(cart.id.isNotBlank())
    }

    @Test
    fun `createNewCart generates unique ids`() {
        val service = createService()

        val firstCart = service.createNewCart()
        val secondCart = service.createNewCart()

        assertNotEquals(firstCart.id, secondCart.id)
    }

    @Test
    fun `new cart starts empty`() {
        val service = createService()

        val cart = service.createNewCart()

        assertTrue(cart.items.isEmpty())
    }

    @Test
    fun `addItemToCart throws when cart does not exist`() {
        val service = createService()
        val item = Item(id = "item-1", amount = Money(1234))

        val exception = assertFailsWith<NoSuchElementException> {
            service.addItemToCart("missing-cart", item)
        }

        assertEquals("Cart with id 'missing-cart' was not found", exception.message)
    }

    @Test
    fun `removeItemFromCart throws when cart does not exist`() {
        val service = createService()

        val exception = assertFailsWith<NoSuchElementException> {
            service.removeItemFromCart("missing-cart", "item-1")
        }

        assertEquals("Cart with id 'missing-cart' was not found", exception.message)
    }

    @Test
    fun `removeItemFromCart is idempotent when item does not exist in cart`() {
        val service = createService()
        val cart = service.createNewCart()

        val result = service.removeItemFromCart(cart.id, "missing-item")

        assertEquals(cart.id, result.id)
        assertTrue(result.items.isEmpty())
    }

    @Test
    fun `addItemToCart is idempotent for the same item id`() {
        val service = createService()
        val cart = service.createNewCart()
        val item = Item(id = "item-1", amount = Money(1234))

        service.addItemToCart(cart.id, item)
        val result = service.addItemToCart(cart.id, item)

        assertEquals(1, result.items.size)
        assertEquals("item-1", result.items[0].id)
        assertEquals(Money(1234), result.items[0].amount)
    }
}