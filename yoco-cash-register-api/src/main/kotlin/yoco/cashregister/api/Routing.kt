package za.co.yoco.cashregister.api

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import za.co.yoco.cashregister.api.domain.service.CashRegisterService

fun Application.configureRouting(
    cashRegisterService: CashRegisterService
) {
    routing {
        get("/") {
            call.respond(cashRegisterService.createNewCart())
        }
    }
}
