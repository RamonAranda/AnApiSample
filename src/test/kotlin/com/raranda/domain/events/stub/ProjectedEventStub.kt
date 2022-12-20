package com.raranda.domain.events.stub

import com.raranda.domain.events.EventId
import com.raranda.domain.events.EventTitle
import com.raranda.domain.events.Price
import com.raranda.domain.events.projection.ProjectedEvent
import java.time.Instant
import kotlin.random.Random

object ProjectedEventStub {
    fun random(
        id: EventId = EventIdStub.random(),
        title: EventTitle = EventTitleStub.random(),
        startDate: Instant = InstantStub.random(),
        endDate: Instant = startDate.plusSeconds(3600),
        minPrice: Price = PriceStub.random(),
        maxPrice: Price = Price(minPrice.value + Random.nextInt().toBigDecimal()),
    ) = ProjectedEvent(
        id = id,
        title = title,
        startDate = startDate,
        endDate = endDate,
        minPrice = minPrice,
        maxPrice = maxPrice,
    )
}
