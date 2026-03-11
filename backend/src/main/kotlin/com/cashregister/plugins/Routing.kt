package com.cashregister.plugins

import com.cashregister.routes.itemRoutes
import com.cashregister.services.ItemService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val itemService = ItemService()
    routing {
        itemRoutes(itemService)
    }
}
