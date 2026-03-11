package com.cashregister.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.LongIdTable
import java.math.BigDecimal

object Items : LongIdTable("items") {
    val name = varchar("name", 255)
    val price = decimal("price", precision = 10, scale = 2)
}

@Serializable
data class Item(
    val id: Long = 0,
    val name: String,
    @Serializable(with = BigDecimalSerializer::class)
    val price: BigDecimal
)

@Serializable
data class CreateItemRequest(
    val name: String,
    @Serializable(with = BigDecimalSerializer::class)
    val price: BigDecimal
)
