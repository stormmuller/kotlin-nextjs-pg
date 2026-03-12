package za.co.yoco.cashregister.api.domain

import kotlinx.serialization.Serializable

@Serializable
data class Cart(
    val id: String,
    val items: List<Item> = emptyList()
)