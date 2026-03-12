package za.co.yoco.cashregister.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.upsert
import za.co.yoco.cashregister.api.Carts
import za.co.yoco.cashregister.api.Items
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

    override fun saveCart(cart: Cart) {
        transaction(database) {
            Carts.upsert { it[id] = cart.id }
            Items.deleteWhere { cartId eq cart.id }
            if (cart.items.isNotEmpty()) {
                Items.batchInsert(cart.items) { item ->
                    this[Items.id] = item.id
                    this[Items.cartId] = cart.id
                    this[Items.amountCents] = item.amount.cents
                }
            }
        }
    }
}
