package za.co.yoco.cashregister.api

import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    val cartRepository = _root_ide_package_.za.co.yoco.cashregister.repository.InMemoryCartRepository()
    val cashRegisterService = _root_ide_package_.za.co.yoco.cashregister.service.CashRegisterServiceImpl(cartRepository)

    configureAdministration()
    configureSerialization()
//    configureDatabases()
    configureMonitoring()
    configureSecurity()
    configureHTTP()
    configureRouting(cashRegisterService)
}
