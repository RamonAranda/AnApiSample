package com.raranda.domain.events.projection.update

import com.raranda.domain.events.EventId
import com.raranda.domain.events.EventTitle
import com.raranda.domain.events.Price
import com.raranda.domain.events.projection.EventProjectionRepository
import com.raranda.domain.events.projection.ProjectedEvent
import com.raranda.domain.providers.fetch.ProviderEvent
import com.raranda.domain.providers.fetch.ProviderEventsFetched
import cqrs.domain.event.EventHandler

class ProjectEventsOnProviderEventsFetched(private val repository: EventProjectionRepository) :
    EventHandler<ProviderEventsFetched> {
    override fun on(event: ProviderEventsFetched) {
        val projectedEvents = event.events.map { it.toProjection() }
        repository.save(projectedEvents)
    }
}

private fun ProviderEvent.toProjection() = ProjectedEvent(
    id = EventId(id),
    title = EventTitle(title),
    startDate = startDate,
    endDate = endDate,
    minPrice = minPrice(),
    maxPrice = maxPrice(),
)

private fun ProviderEvent.minPrice(): Price = Price(zones.minOf { it.price })
private fun ProviderEvent.maxPrice(): Price = Price(zones.maxOf { it.price })
