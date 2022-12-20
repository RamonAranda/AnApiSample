package com.raranda.domain.events.projection.get

import java.math.BigDecimal

data class GetEventsResponse(val events: List<EventResponse>)

data class EventResponse(
    val id: String,
    val title: String,
    val startDate: String,
    val startTime: String,
    val endDate: String,
    val endTime: String,
    val minPrice: BigDecimal,
    val maxPrice: BigDecimal
)
