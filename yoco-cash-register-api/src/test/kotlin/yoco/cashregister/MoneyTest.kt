package za.co.yoco.cashregister

import za.co.yoco.cashregister.domain.Money
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class MoneyTest {

    @Test
    fun createsMoneyFromCents() {
        val money = _root_ide_package_.za.co.yoco.cashregister.domain.Money(575)

        assertEquals(575L, money.cents)
        assertEquals("R 5.75", money.toString())
    }

    @Test
    fun createsMoneyFromDollarsAndCents() {
        val money = _root_ide_package_.za.co.yoco.cashregister.domain.Money(5, 75)

        assertEquals(575L, money.cents)
        assertEquals("R 5.75", money.toString())
    }

    @Test
    fun addsMoneyAmounts() {
        val result = Money(5, 75) + Money(2, 25)

        assertEquals(Money(800), result)
        assertEquals("R 8.00", result.toString())
    }

    @Test
    fun subtractsMoneyAmounts() {
        val result = Money(5, 75) - Money(2, 25)

        assertEquals(Money(350), result)
        assertEquals("R 3.50", result.toString())
    }

    @Test
    fun formatsNegativeAmounts() {
        val money = Money(-125)

        assertEquals("R -1.25", money.toString())
    }

    @Test
    fun formatsPaddedAmounts() {
        val money1 = Money(105)
        val money2 = Money(150)
        val money3 = Money(15)

        assertEquals("R 1.05", money1.toString())
        assertEquals("R 1.50", money2.toString())
        assertEquals("R 0.15", money3.toString())
    }

    @Test
    fun formatsZeroAmount() {
        val money = Money(0)

        assertEquals("R 0.00", money.toString())
    }

    @Test
    fun supportsValueEquality() {
        val first = Money(575)
        val second = Money(5, 75)

        assertEquals(first, second)
        assertEquals(first.hashCode(), second.hashCode())
    }

    @Test
    fun distinguishesDifferentValues() {
        val first = Money(575)
        val second = Money(576)

        assertNotEquals(first, second)
    }
}