package com.raranda.domain.events.behavior

import com.raranda.adapter.output.events.EventProjectionRepositoryInMemory
import com.raranda.domain.events.projection.get.EventResponse
import com.raranda.domain.events.projection.get.EventsGetter
import com.raranda.domain.events.projection.get.GetEvents
import com.raranda.domain.events.projection.get.GetEventsResponse
import com.raranda.domain.events.stub.InstantStub
import com.raranda.domain.events.stub.ProjectedEventStub
import com.raranda.domain.events.stub.plusDays
import cqrs.infrastructure.query.SynchronousQueryBus
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class EventsGetterTest : ShouldSpec({
    val bus = SynchronousQueryBus()
    val repository = EventProjectionRepositoryInMemory()
    val handler = EventsGetter(repository)
    bus.register(handler)

    should("find nothing when there are no events") {
        val now = InstantStub.random()
        val query = GetEvents(
            startDate = now.minusSeconds(10),
            endDate = now.plusSeconds(10)
        )
        bus.handle<GetEventsResponse>(query) shouldBe GetEventsResponse(emptyList())
    }

    should("find no events mathing the desired date range") {
        val now = InstantStub.of("2022-11-22T00:00:00Z")
        val futureEvents = listOf(
            ProjectedEventStub.random(
                startDate = now,
                endDate = now.plusDays(2),
            ),
            ProjectedEventStub.random(
                startDate = now,
                endDate = now.plusDays(3),
            ),
        )
        repository.save(futureEvents)

        val query = GetEvents(
            startDate = now.plusDays(4),
            endDate = now.plusDays(5)
        )
        bus.handle<GetEventsResponse>(query) shouldBe GetEventsResponse(emptyList())
    }
    should("find an event mathing the desired date range") {
        val now = InstantStub.of("2022-11-22T00:00:00Z")
        val futureEvents = listOf(
            ProjectedEventStub.random(
                startDate = now.plusDays(2),
                endDate = now.plusDays(4),
            ),
            ProjectedEventStub.random(
                startDate = now.plusDays(3),
                endDate = now.plusDays(6),
            ),
            ProjectedEventStub.random(
                startDate = now,
                endDate = now.plusDays(1),
            ),
            ProjectedEventStub.random(
                startDate = now.plusDays(5),
                endDate = now.plusDays(6),
            ),
        )
        repository.save(futureEvents)

        val query = GetEvents(
            startDate = now.plusDays(2),
            endDate = now.plusDays(4)
        )
        bus.handle<GetEventsResponse>(query) shouldBe GetEventsResponse(
            listOf(
                EventResponse(
                    id = futureEvents.first().id.value,
                    title = futureEvents.first().title.value,
                    startDate = "2022-11-24",
                    startTime = "00:00:00",
                    endDate = "2022-11-26",
                    endTime = "00:00:00",
                    minPrice = futureEvents.first().minPrice.value,
                    maxPrice = futureEvents.first().maxPrice.value
                ),
                EventResponse(
                    id = futureEvents[1].id.value,
                    title = futureEvents[1].title.value,
                    startDate = "2022-11-25",
                    startTime = "00:00:00",
                    endDate = "2022-11-28",
                    endTime = "00:00:00",
                    minPrice = futureEvents[1].minPrice.value,
                    maxPrice = futureEvents[1].maxPrice.value
                )
            )
        )
    }
})
