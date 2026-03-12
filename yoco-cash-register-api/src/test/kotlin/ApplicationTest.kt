package za.co

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import za.co.yoco.cashregister.domain.Cart
import za.co.yoco.cashregister.module
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApplicationTest {

    private val json = Json { ignoreUnknownKeys = true }

    private fun testEnvironment() = MapApplicationConfig(
        "postgres.url" to "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        "postgres.user" to "sa",
        "postgres.password" to ""
    )

    private fun HttpRequestBuilder.csrfHeaders() {
        header(HttpHeaders.Origin, "http://localhost:8080")
        header(HttpHeaders.Host, "localhost:8080")
        header("X-CSRF-Token", "test")
    }

    @Test
    fun `POST carts creates a new cart`() = testApplication {
        environment { config = testEnvironment() }
        application { module() }

        val response = client.post("/carts") { csrfHeaders() }

        assertEquals(HttpStatusCode.Created, response.status)
        val cart = json.decodeFromString<Cart>(response.bodyAsText())
        assertTrue(cart.id.isNotBlank())
        assertTrue(cart.items.isEmpty())
    }

    @Test
    fun `PUT carts cartId items itemId adds item to cart`() = testApplication {
        environment { config = testEnvironment() }
        application { module() }

        val cart = json.decodeFromString<Cart>(client.post("/carts") { csrfHeaders() }.bodyAsText())

        val response = client.put("/carts/${cart.id}/items/item-1") {
            csrfHeaders()
            contentType(ContentType.Application.Json)
            setBody("""{"amountCents":1000}""")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val updatedCart = json.decodeFromString<Cart>(response.bodyAsText())
        assertEquals(1, updatedCart.items.size)
        assertEquals("item-1", updatedCart.items[0].id)
    }

    @Test
    fun `PUT carts cartId items itemId is idempotent`() = testApplication {
        environment { config = testEnvironment() }
        application { module() }

        val cart = json.decodeFromString<Cart>(client.post("/carts") { csrfHeaders() }.bodyAsText())

        repeat(3) {
            client.put("/carts/${cart.id}/items/item-1") {
                csrfHeaders()
                contentType(ContentType.Application.Json)
                setBody("""{"amountCents":1000}""")
            }
        }
        val finalCart = json.decodeFromString<Cart>(
            client.put("/carts/${cart.id}/items/item-1") {
                csrfHeaders()
                contentType(ContentType.Application.Json)
                setBody("""{"amountCents":1000}""")
            }.bodyAsText()
        )

        assertEquals(1, finalCart.items.size)
    }

    @Test
    fun `DELETE carts cartId items itemId removes item from cart`() = testApplication {
        environment { config = testEnvironment() }
        application { module() }

        val cart = json.decodeFromString<Cart>(client.post("/carts") { csrfHeaders() }.bodyAsText())
        client.put("/carts/${cart.id}/items/item-1") {
            csrfHeaders()
            contentType(ContentType.Application.Json)
            setBody("""{"amountCents":1000}""")
        }

        val response = client.delete("/carts/${cart.id}/items/item-1") { csrfHeaders() }

        assertEquals(HttpStatusCode.OK, response.status)
        val updatedCart = json.decodeFromString<Cart>(response.bodyAsText())
        assertTrue(updatedCart.items.isEmpty())
    }

    @Test
    fun `DELETE carts cartId items itemId is idempotent when item does not exist`() = testApplication {
        environment { config = testEnvironment() }
        application { module() }

        val cart = json.decodeFromString<Cart>(client.post("/carts") { csrfHeaders() }.bodyAsText())

        val response = client.delete("/carts/${cart.id}/items/missing-item") { csrfHeaders() }

        assertEquals(HttpStatusCode.OK, response.status)
        val updatedCart = json.decodeFromString<Cart>(response.bodyAsText())
        assertTrue(updatedCart.items.isEmpty())
    }

    @Test
    fun `PUT carts cartId items itemId returns 404 when cart does not exist`() = testApplication {
        environment { config = testEnvironment() }
        application { module() }

        val response = client.put("/carts/missing-cart/items/item-1") {
            csrfHeaders()
            contentType(ContentType.Application.Json)
            setBody("""{"amountCents":1000}""")
        }

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `DELETE carts cartId items itemId returns 404 when cart does not exist`() = testApplication {
        environment { config = testEnvironment() }
        application { module() }

        val response = client.delete("/carts/missing-cart/items/item-1") { csrfHeaders() }

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `PUT carts cartId items itemId returns 400 when amountCents is negative`() = testApplication {
        environment { config = testEnvironment() }
        application { module() }

        val cart = json.decodeFromString<Cart>(client.post("/carts") { csrfHeaders() }.bodyAsText())

        val response = client.put("/carts/${cart.id}/items/item-1") {
            csrfHeaders()
            contentType(ContentType.Application.Json)
            setBody("""{"amountCents":-1}""")
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `PUT carts cartId items itemId returns 400 when amountCents exceeds maximum`() = testApplication {
        environment { config = testEnvironment() }
        application { module() }

        val cart = json.decodeFromString<Cart>(client.post("/carts") { csrfHeaders() }.bodyAsText())

        val response = client.put("/carts/${cart.id}/items/item-1") {
            csrfHeaders()
            contentType(ContentType.Application.Json)
            setBody("""{"amountCents":100000001}""")
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }
}
