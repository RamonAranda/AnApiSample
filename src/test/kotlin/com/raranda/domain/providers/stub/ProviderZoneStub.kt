package com.raranda.domain.providers.stub

import com.raranda.domain.providers.fetch.ProviderZone
import java.math.BigDecimal
import java.util.UUID
import kotlin.random.Random

object ProviderZoneStub {
    fun random(
        id: String = UUID.randomUUID().toString(),
        capacity: Int = Random.nextInt(0, 100),
        price: BigDecimal = Random.nextDouble(0.0, 100.0).toBigDecimal(),
        name: String = "Zone $id",
        numbered: Boolean = Random.nextBoolean()
    ) = ProviderZone(
        id = id,
        capacity = capacity,
        price = price,
        name = name,
        numbered = numbered,
    )
}
