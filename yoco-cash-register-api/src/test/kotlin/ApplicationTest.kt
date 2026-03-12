package za.co

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import za.co.yoco.cashregister.module
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun testRoot() = testApplication {
        environment {
            config = MapApplicationConfig(
                "postgres.url" to "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
                "postgres.user" to "sa",
                "postgres.password" to ""
            )
        }
        application {
            module()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

}
