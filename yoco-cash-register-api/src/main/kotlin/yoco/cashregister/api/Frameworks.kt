package za.co.yoco.cashregister.api

import io.ktor.server.application.*
import io.ktor.server.plugins.di.*

fun Application.configureFrameworks() {
    dependencies {
        provide { GreetingService { "Hello, World!" } }
    }
}
