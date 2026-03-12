package za.co.yoco.cashregister.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.upsert
import za.co.yoco.cashregister.domain.Cart
import za.co.yoco.cashregister.domain.Item
import za.co.yoco.cashregister.domain.Money
import za.co.yoco.cashregister.domain.repository.CartRepository

class PostgresCartRepository(private val database: Database) : CartRepository {

    override fun getCart(id: String): Cart? {
        return transaction(database) {
            val cartRow = Carts.selectAll().where { Carts.id eq id }.singleOrNull()
                ?: return@transaction null
            val items = Items.selectAll().where { Items.cartId eq id }.map { row ->
                Item(id = row[Items.id], amount = Money(row[Items.amountCents]))
            }
            Cart(id = cartRow[Carts.id], items = items)
        }
    }

    override fun createCart(): Cart {
        return transaction(database) {
            val stmt = Carts.insert { }
            Cart(id = stmt[Carts.id])
        }
    }

    override fun saveCart(cart: Cart): Cart {
        return transaction(database) {
            Carts.upsert { it[id] = cart.id }
            Items.deleteWhere { cartId eq cart.id }
            val savedItems = if (cart.items.isNotEmpty()) {
                cart.items.map { item ->
                    val savedItem = item.withGeneratedId()
                    Items.insert {
                        it[id] = savedItem.id
                        it[cartId] = cart.id
                        it[amountCents] = savedItem.amount.cents
                    }
                    savedItem
                }
            } else emptyList()
            cart.copy(items = savedItems)
        }
    }
}
