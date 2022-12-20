package com.raranda.domain.events.behavior

import com.raranda.adapter.output.events.EventProjectionRepositoryInMemory
import com.raranda.domain.events.EventId
import com.raranda.domain.events.EventTitle
import com.raranda.domain.events.Price
import com.raranda.domain.events.projection.ProjectedEvent
import com.raranda.domain.events.projection.update.ProjectEventsOnProviderEventsFetched
import com.raranda.domain.providers.fetch.ProviderEventsFetched
import com.raranda.domain.providers.stub.ProviderEventStub
import com.raranda.domain.providers.stub.ProviderZoneStub
import cqrs.infrastructure.event.SynchronousEventBus
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class ProjectEventsOnProviderEventsFetchedTest : ShouldSpec({
    val bus = SynchronousEventBus()
    val repository = EventProjectionRepositoryInMemory()
    val handler = ProjectEventsOnProviderEventsFetched(repository)
    bus.register(handler)

    should("project events") {
        val events = listOf(
            ProviderEventStub.random(zones = listOf(
                ProviderZoneStub.random(price = 10.0.toBigDecimal()),
                ProviderZoneStub.random(price = 20.0.toBigDecimal()),
            )),
            ProviderEventStub.random(zones = listOf(
                ProviderZoneStub.random(price = 40.0.toBigDecimal()),
                ProviderZoneStub.random(price = 60.0.toBigDecimal()),
            ))
        )
        bus.publish(ProviderEventsFetched(events))
        repository.all() shouldBe listOf(
            ProjectedEvent(
                id = EventId(events.first().id),
                title = EventTitle(events.first().title),
                startDate = events.first().startDate,
                endDate = events.first().endDate,
                minPrice = Price(10.0.toBigDecimal()),
                maxPrice = Price(20.0.toBigDecimal()),
            ),
            ProjectedEvent(
                id = EventId(events.last().id),
                title = EventTitle(events.last().title),
                startDate = events.last().startDate,
                endDate = events.last().endDate,
                minPrice = Price(40.0.toBigDecimal()),
                maxPrice = Price(60.0.toBigDecimal()),
            )
        )
    }
})
