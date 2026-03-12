package za.co.yoco.cashregister.api

import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain
import za.co.yoco.cashregister.api.service.CashRegisterServiceImpl

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    val cashRegisterService = CashRegisterServiceImpl()

    configureAdministration()
    configureSerialization()
    configureDatabases()
    configureMonitoring()
    configureSecurity()
    configureHTTP()
    configureRouting(cashRegisterService)
}
