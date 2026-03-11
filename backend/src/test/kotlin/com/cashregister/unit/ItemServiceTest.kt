package com.cashregister.unit

import com.cashregister.models.CreateItemRequest
import com.cashregister.models.Items
import com.cashregister.services.ItemService
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ItemServiceTest {

    private lateinit var itemService: ItemService

    @Before
    fun setUp() {
        Database.connect(
            url = "jdbc:h2:mem:testunit;DB_CLOSE_DELAY=-1;MODE=PostgreSQL;NON_KEYWORDS=VALUE",
            driver = "org.h2.Driver",
            user = "sa",
            password = ""
        )
        transaction {
            SchemaUtils.create(Items)
        }
        itemService = ItemService()
    }

    @After
    fun tearDown() {
        transaction {
            SchemaUtils.drop(Items)
        }
    }

    @Test
    fun `getAllItems returns empty list when no items exist`() {
        val items = itemService.getAllItems()
        assertTrue(items.isEmpty())
    }

    @Test
    fun `createItem stores and returns item with assigned id`() {
        val request = CreateItemRequest(name = "Apple", price = BigDecimal("1.99"))
        val created = itemService.createItem(request)

        assertTrue(created.id > 0)
        assertEquals("Apple", created.name)
        assertEquals(BigDecimal("1.99"), created.price)
    }

    @Test
    fun `getAllItems returns all created items`() {
        itemService.createItem(CreateItemRequest("Apple", BigDecimal("1.99")))
        itemService.createItem(CreateItemRequest("Banana", BigDecimal("0.99")))

        val items = itemService.getAllItems()
        assertEquals(2, items.size)
    }

    @Test
    fun `getItemById returns item when it exists`() {
        val created = itemService.createItem(CreateItemRequest("Apple", BigDecimal("1.99")))
        val found = itemService.getItemById(created.id)

        assertNotNull(found)
        assertEquals(created.id, found.id)
        assertEquals("Apple", found.name)
    }

    @Test
    fun `getItemById returns null when item does not exist`() {
        val found = itemService.getItemById(9999L)
        assertNull(found)
    }

    @Test
    fun `deleteItem returns true and removes the item`() {
        val created = itemService.createItem(CreateItemRequest("Apple", BigDecimal("1.99")))
        val deleted = itemService.deleteItem(created.id)

        assertTrue(deleted)
        assertNull(itemService.getItemById(created.id))
    }

    @Test
    fun `deleteItem returns false when item does not exist`() {
        val deleted = itemService.deleteItem(9999L)
        assertFalse(deleted)
    }

    @Test
    fun `price is stored with correct precision`() {
        val request = CreateItemRequest(name = "Widget", price = BigDecimal("19.99"))
        val created = itemService.createItem(request)
        val fetched = itemService.getItemById(created.id)

        assertNotNull(fetched)
        assertEquals(0, BigDecimal("19.99").compareTo(fetched.price))
    }
}
