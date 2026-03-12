package za.co.yoco.cashregister.api.domain

import kotlinx.serialization.Serializable
import za.co.yoco.cashregister.api.domain.Money

@Serializable
data class Item(
    val id: String = "", // TODO: This could be more type-safe by using UUID or similar. But I am not sure what we will use here yet.
    val amount: Money
)