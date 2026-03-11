package com.cashregister.plugins

import com.cashregister.models.Items
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    val dbUrl = environment.config.propertyOrNull("database.url")?.getString()
        ?: "jdbc:postgresql://localhost:5432/cashregister"
    val dbUser = environment.config.propertyOrNull("database.user")?.getString() ?: "postgres"
    val dbPassword = environment.config.propertyOrNull("database.password")?.getString() ?: "postgres"
    val dbDriver = environment.config.propertyOrNull("database.driver")?.getString()
        ?: "org.postgresql.Driver"

    val hikariConfig = HikariConfig().apply {
        jdbcUrl = dbUrl
        username = dbUser
        password = dbPassword
        driverClassName = dbDriver
        maximumPoolSize = 10
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    }

    val dataSource = HikariDataSource(hikariConfig)
    Database.connect(dataSource)

    transaction {
        SchemaUtils.create(Items)
    }
}
