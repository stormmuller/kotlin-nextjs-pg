package com.cashregister.services

import com.cashregister.models.CreateItemRequest
import com.cashregister.models.Item
import com.cashregister.models.Items
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class ItemService {
    fun getAllItems(): List<Item> = transaction {
        Items.selectAll().map { row ->
            Item(
                id = row[Items.id].value,
                name = row[Items.name],
                price = row[Items.price]
            )
        }
    }

    fun getItemById(id: Long): Item? = transaction {
        Items.selectAll().where { Items.id eq id }
            .map { row ->
                Item(
                    id = row[Items.id].value,
                    name = row[Items.name],
                    price = row[Items.price]
                )
            }
            .singleOrNull()
    }

    fun createItem(request: CreateItemRequest): Item = transaction {
        val insertedId = Items.insertAndGetId { row ->
            row[Items.name] = request.name
            row[Items.price] = request.price
        }
        Item(
            id = insertedId.value,
            name = request.name,
            price = request.price
        )
    }

    fun deleteItem(id: Long): Boolean = transaction {
        Items.deleteWhere { Items.id eq id } > 0
    }
}
