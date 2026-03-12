package za.co.yoco.cashregister

import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain
import za.co.yoco.cashregister.api.configureAdministration
import za.co.yoco.cashregister.api.configureHTTP
import za.co.yoco.cashregister.api.configureMonitoring
import za.co.yoco.cashregister.api.configureRouting
import za.co.yoco.cashregister.api.configureSecurity
import za.co.yoco.cashregister.api.configureSerialization
import za.co.yoco.cashregister.repository.InMemoryCartRepository
import za.co.yoco.cashregister.service.CashRegisterServiceImpl

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    val cartRepository = InMemoryCartRepository()
    val cashRegisterService = CashRegisterServiceImpl(cartRepository)

    configureAdministration()
    configureSerialization()
//    configureDatabases()
    configureMonitoring()
    configureSecurity()
    configureHTTP()
    configureRouting(cashRegisterService)
}
