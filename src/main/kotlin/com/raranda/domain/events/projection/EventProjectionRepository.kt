package com.raranda.domain.events.projection

import java.time.Instant

interface EventProjectionRepository {
    fun findBy(startDate: Instant, endDate: Instant): List<ProjectedEvent>
    fun save(events: List<ProjectedEvent>)
}
