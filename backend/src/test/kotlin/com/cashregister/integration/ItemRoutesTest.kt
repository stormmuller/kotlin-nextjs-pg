package com.cashregister.integration

import com.cashregister.models.Items
import com.cashregister.plugins.*
import com.cashregister.routes.itemRoutes
import com.cashregister.services.ItemService
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ItemRoutesTest {

    @Before
    fun setUp() {
        Database.connect(
            url = "jdbc:h2:mem:testintegration;DB_CLOSE_DELAY=-1;MODE=PostgreSQL;NON_KEYWORDS=VALUE",
            driver = "org.h2.Driver",
            user = "sa",
            password = ""
        )
        transaction {
            SchemaUtils.create(Items)
        }
    }

    @After
    fun tearDown() {
        transaction {
            SchemaUtils.drop(Items)
        }
    }

    private fun Application.testModule() {
        configureSecurity()
        configureRateLimit()
        configureSerialization()
        configureLogging()
        val itemService = ItemService()
        routing {
            itemRoutes(itemService)
        }
    }

    @Test
    fun `GET items returns empty list`() = testApplication {
        application { testModule() }

        val response = client.get("/items")
        assertEquals(HttpStatusCode.OK, response.status)
        val body = Json.parseToJsonElement(response.bodyAsText())
        assertTrue(body.jsonArray.isEmpty())
    }

    @Test
    fun `POST items creates item and returns 201`() = testApplication {
        application { testModule() }

        val client = createClient {
            install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/items") {
            contentType(ContentType.Application.Json)
            setBody("""{"name":"Apple","price":"1.99"}""")
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val body = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        assertEquals("Apple", body["name"]?.jsonPrimitive?.content)
        assertEquals("1.99", body["price"]?.jsonPrimitive?.content)
    }

    @Test
    fun `POST items returns 400 for blank name`() = testApplication {
        application { testModule() }

        val response = client.post("/items") {
            contentType(ContentType.Application.Json)
            setBody("""{"name":"","price":"1.99"}""")
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `POST items returns 400 for zero price`() = testApplication {
        application { testModule() }

        val response = client.post("/items") {
            contentType(ContentType.Application.Json)
            setBody("""{"name":"Apple","price":"0"}""")
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `DELETE items removes item and returns 204`() = testApplication {
        application { testModule() }

        val postResponse = client.post("/items") {
            contentType(ContentType.Application.Json)
            setBody("""{"name":"Apple","price":"1.99"}""")
        }
        val body = Json.parseToJsonElement(postResponse.bodyAsText()).jsonObject
        val id = body["id"]?.jsonPrimitive?.content

        val deleteResponse = client.delete("/items/$id")
        assertEquals(HttpStatusCode.NoContent, deleteResponse.status)
    }

    @Test
    fun `DELETE items returns 404 for nonexistent item`() = testApplication {
        application { testModule() }

        val response = client.delete("/items/9999")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `DELETE items returns 400 for invalid id`() = testApplication {
        application { testModule() }

        val response = client.delete("/items/notanid")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `GET items returns all items after creation`() = testApplication {
        application { testModule() }

        client.post("/items") {
            contentType(ContentType.Application.Json)
            setBody("""{"name":"Apple","price":"1.99"}""")
        }
        client.post("/items") {
            contentType(ContentType.Application.Json)
            setBody("""{"name":"Banana","price":"0.99"}""")
        }

        val response = client.get("/items")
        assertEquals(HttpStatusCode.OK, response.status)
        val body = Json.parseToJsonElement(response.bodyAsText()).jsonArray
        assertEquals(2, body.size)
    }
}
