package za.co.yoco.cashregister.domain

import kotlinx.serialization.Serializable

@Serializable
data class Cart(
    val id: String,
    val items: List<za.co.yoco.cashregister.domain.Item> = emptyList()
)