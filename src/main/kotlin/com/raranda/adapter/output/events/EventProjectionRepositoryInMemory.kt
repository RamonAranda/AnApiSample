package com.raranda.adapter.output.events

import com.raranda.domain.events.projection.EventProjectionRepository
import com.raranda.domain.events.projection.ProjectedEvent
import java.time.Instant

class EventProjectionRepositoryInMemory(private var events: List<ProjectedEvent> = listOf()) :
    EventProjectionRepository {

    override fun findBy(startDate: Instant, endDate: Instant): List<ProjectedEvent> =
        events.filter {
            startDate >= it.startDate && startDate <= it.endDate ||
                endDate >= it.startDate && endDate <= it.endDate ||
                    startDate <= it.startDate && endDate >= it.endDate
        }

    override fun save(events: List<ProjectedEvent>) {
        this.events = events
    }

    fun all(): List<ProjectedEvent> = events
}
