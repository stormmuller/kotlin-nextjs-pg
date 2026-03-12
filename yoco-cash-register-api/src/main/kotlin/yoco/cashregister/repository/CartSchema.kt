package za.co.yoco.cashregister.repository

import org.jetbrains.exposed.sql.Table
import za.co.yoco.cashregister.domain.Item
import java.util.UUID

object Carts : Table("carts") {
    val id = varchar("id", 36).clientDefault { UUID.randomUUID().toString() }
    override val primaryKey = PrimaryKey(id)
}

object Items : Table("items") {
    val id = varchar("id", 36)
    val cartId = varchar("cart_id", 36).references(Carts.id)
    // Amounts are stored in cents. Must be non-negative with a maximum of 100,000,000 cents (R 1,000,000.00).
    val amountCents = long("amount_cents")
    override val primaryKey = PrimaryKey(cartId, id)

    init {
        check("items_amount_cents_non_negative") { amountCents greaterEq 0L }
        check("items_amount_cents_max") { amountCents lessEq 100_000_000L }
    }
}

internal fun Item.withGeneratedId(): Item = if (id.isBlank()) copy(id = UUID.randomUUID().toString()) else this
