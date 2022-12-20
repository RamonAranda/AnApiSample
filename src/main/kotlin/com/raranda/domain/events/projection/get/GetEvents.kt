package com.raranda.domain.events.projection.get

import cqrs.domain.query.Query
import java.time.Instant

data class GetEvents(val startDate: Instant, val endDate: Instant) : Query
