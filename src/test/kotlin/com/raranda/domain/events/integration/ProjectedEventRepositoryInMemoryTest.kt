package com.raranda.domain.events.integration

import com.raranda.adapter.output.events.EventProjectionRepositoryInMemory
import com.raranda.domain.events.projection.ProjectedEvent
import com.raranda.domain.events.stub.InstantStub
import com.raranda.domain.events.stub.ProjectedEventStub
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class ProjectedEventRepositoryInMemoryTest : ShouldSpec({
    should("retrieve a list of events") {
        val events = listOf(
            ProjectedEventStub.random(
                startDate = InstantStub.of("2022-11-21T00:00:00Z"),
                endDate = InstantStub.of("2022-11-26T00:00:00Z"),
            ),
            ProjectedEventStub.random(
                startDate = InstantStub.of("2022-11-20T00:00:00Z"),
                endDate = InstantStub.of("2022-11-22T13:00:00Z"),
            ),
            ProjectedEventStub.random(
                startDate = InstantStub.of("2022-11-25T00:00:00Z"),
                endDate = InstantStub.of("2022-11-26T11:00:00Z"),
            ),
            ProjectedEventStub.random(
                startDate = InstantStub.of("2022-11-19T00:00:00Z"),
                endDate = InstantStub.of("2022-11-21T00:00:00Z"),
            )
        )
        val expected = events.dropLast(2)
        val startDate = InstantStub.of("2022-11-21T10:00:00Z")
        val endDate = InstantStub.of("2022-11-22T12:00:00Z")
        val repository = EventProjectionRepositoryInMemory(events)
        repository.findBy(startDate, endDate) shouldBe expected
    }
    should("retrieve an empty list of events as there are no events stored") {
        val events = listOf<ProjectedEvent>()
        val repository = EventProjectionRepositoryInMemory(events)
        val startDate = InstantStub.random()
        val endDate = InstantStub.random()
        repository.findBy(startDate, endDate) shouldBe emptyList()
    }
    should("retrieve an empty list of events as date range does not intersect with any event") {
        val events = listOf(
            ProjectedEventStub.random(
                startDate = InstantStub.of("2022-11-20T00:00:00Z"),
                endDate = InstantStub.of("2022-11-21T00:00:00Z"),
            ),
            ProjectedEventStub.random(
                startDate = InstantStub.of("2022-11-22T12:00:00Z"),
                endDate = InstantStub.of("2022-11-23T00:00:00Z"),
            )
        )
        val repository = EventProjectionRepositoryInMemory(events)
        val startDate = InstantStub.of("2022-11-21T12:00:00Z")
        val endDate = InstantStub.of("2022-11-22T11:00:00Z")
        repository.findBy(startDate, endDate) shouldBe emptyList()
    }
})
