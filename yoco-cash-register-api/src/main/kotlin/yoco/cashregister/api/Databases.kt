package za.co.yoco.cashregister.api

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases(): Database {
    val url = environment.config.property("postgres.url").getString()
    val user = environment.config.property("postgres.user").getString()
    val password = environment.config.property("postgres.password").getString()

    val driver = when {
        url.startsWith("jdbc:postgresql:") -> "org.postgresql.Driver"
        url.startsWith("jdbc:h2:") -> "org.h2.Driver"
        else -> throw IllegalArgumentException("Unsupported database URL: $url")
    }

    val database = Database.connect(
        url = url,
        driver = driver,
        user = user,
        password = password,
    )

    transaction(database) {
        SchemaUtils.create(Carts, Items)
    }

    return database
}
