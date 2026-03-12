package za.co.yoco.cashregister.api

import io.ktor.server.application.*
import io.ktor.server.routing.*
import za.co.yoco.cashregister.domain.service.CashRegisterService

fun Application.configureRouting(
    cashRegisterService: CashRegisterService
) {
    routing {
        cartRoutes(cashRegisterService)
    }
}
