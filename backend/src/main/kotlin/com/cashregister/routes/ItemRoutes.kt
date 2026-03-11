package com.cashregister.routes

import com.cashregister.models.CreateItemRequest
import com.cashregister.services.ItemService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.math.BigDecimal

fun Route.itemRoutes(itemService: ItemService) {
    route("/items") {
        get {
            call.respond(itemService.getAllItems())
        }

        post {
            val request = call.receive<CreateItemRequest>()
            if (request.name.isBlank()) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Name cannot be blank"))
                return@post
            }
            if (request.price <= BigDecimal.ZERO) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Price must be positive"))
                return@post
            }
            val item = itemService.createItem(request)
            call.respond(HttpStatusCode.Created, item)
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
                ?: return@delete call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid ID"))
            val deleted = itemService.deleteItem(id)
            if (deleted) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Item not found"))
            }
        }
    }
}
