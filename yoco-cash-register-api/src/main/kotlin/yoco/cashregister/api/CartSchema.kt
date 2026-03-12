package za.co.yoco.cashregister.api

import org.jetbrains.exposed.sql.Table

object Carts : Table("carts") {
    val id = varchar("id", 36)
    override val primaryKey = PrimaryKey(id)
}

object Items : Table("items") {
    val id = varchar("id", 36)
    val cartId = varchar("cart_id", 36).references(Carts.id)
    val amountCents = long("amount_cents")
    override val primaryKey = PrimaryKey(cartId, id)
}
