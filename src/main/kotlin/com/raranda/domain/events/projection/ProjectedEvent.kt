package com.raranda.domain.events.projection

import com.raranda.domain.events.EventId
import com.raranda.domain.events.EventTitle
import com.raranda.domain.events.Price
import java.time.Instant

data class ProjectedEvent(
    val id: EventId,
    val title: EventTitle,
    val startDate: Instant,
    val endDate: Instant,
    val minPrice: Price,
    val maxPrice: Price
)
