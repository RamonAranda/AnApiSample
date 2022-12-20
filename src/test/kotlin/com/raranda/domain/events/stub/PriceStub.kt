package com.raranda.domain.events.stub

import com.raranda.domain.events.Price
import kotlin.random.Random

object PriceStub {
    fun random() = Price(Random.nextFloat().toBigDecimal())
}
