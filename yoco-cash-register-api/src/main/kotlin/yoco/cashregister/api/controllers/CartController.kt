package za.co.yoco.cashregister.api

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import za.co.yoco.cashregister.domain.Item
import za.co.yoco.cashregister.domain.Money
import za.co.yoco.cashregister.domain.service.CashRegisterService

private const val MAX_AMOUNT_CENTS = 100_000_000L

@Serializable
private data class ItemRequest(val amountCents: Long)

fun Route.cartRoutes(cashRegisterService: CashRegisterService) {
    post("/carts") {
        call.respond(HttpStatusCode.Created, cashRegisterService.createNewCart())
    }

    put("/carts/{cartId}/items/{itemId}") {
        val cartId = call.parameters["cartId"] ?: return@put call.respond(HttpStatusCode.BadRequest, "Missing cartId")
        val itemId = call.parameters["itemId"] ?: return@put call.respond(HttpStatusCode.BadRequest, "Missing itemId")
        val request = call.receive<ItemRequest>()
        if (request.amountCents < 0) {
            return@put call.respond(HttpStatusCode.BadRequest, "amountCents must be non-negative")
        }
        if (request.amountCents > MAX_AMOUNT_CENTS) {
            return@put call.respond(HttpStatusCode.BadRequest, "amountCents must not exceed $MAX_AMOUNT_CENTS")
        }
        try {
            val cart = cashRegisterService.addItemToCart(cartId, Item(id = itemId, amount = Money(request.amountCents)))
            call.respond(HttpStatusCode.OK, cart)
        } catch (e: NoSuchElementException) {
            call.respond(HttpStatusCode.NotFound, e.message ?: "Not found")
        }
    }

    delete("/carts/{cartId}/items/{itemId}") {
        val cartId = call.parameters["cartId"] ?: return@delete call.respond(HttpStatusCode.BadRequest, "Missing cartId")
        val itemId = call.parameters["itemId"] ?: return@delete call.respond(HttpStatusCode.BadRequest, "Missing itemId")
        try {
            val cart = cashRegisterService.removeItemFromCart(cartId, itemId)
            call.respond(HttpStatusCode.OK, cart)
        } catch (e: NoSuchElementException) {
            call.respond(HttpStatusCode.NotFound, e.message ?: "Not found")
        }
    }
}
