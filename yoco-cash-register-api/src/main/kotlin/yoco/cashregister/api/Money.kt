package za.co.yoco.cashregister.api

import kotlin.math.abs

@JvmInline
value class Money(val cents: Long) {

    constructor(whole: Long, cents: Int) : this(whole * 100 + cents)

    operator fun plus(other: Money): Money {
        return Money(cents + other.cents)
    }

    operator fun minus(other: Money): Money {
        return Money(cents - other.cents)
    }

    override fun toString(): String {
        val absoluteCents = abs(cents)
        val whole = absoluteCents / 100
        val fractional = absoluteCents % 100
        val sign = if (this.cents < 0) "-" else ""

        return "R $sign$whole.${fractional.toString().padStart(2, '0')}"
    }
}
