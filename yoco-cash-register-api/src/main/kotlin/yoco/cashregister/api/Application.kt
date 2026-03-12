package za.co.yoco.cashregister.api

import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain
import za.co.yoco.cashregister.api.repository.InMemoryCartRepository
import za.co.yoco.cashregister.api.service.CashRegisterServiceImpl

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    val cartRepository = InMemoryCartRepository()
    val cashRegisterService = CashRegisterServiceImpl(cartRepository)

    configureAdministration()
    configureSerialization()
    configureDatabases()
    configureMonitoring()
    configureSecurity()
    configureHTTP()
    configureRouting(cashRegisterService)
}
