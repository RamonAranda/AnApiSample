package com.raranda.domain.providers.behavior

import com.raranda.domain.providers.EventsExternalProvider
import com.raranda.domain.providers.fetch.FetchProviderEvents
import com.raranda.domain.providers.fetch.ProviderEvent
import com.raranda.domain.providers.fetch.ProviderEventsFetched
import com.raranda.domain.providers.fetch.ProviderEventsFetcher
import com.raranda.domain.providers.stub.ProviderEventStub
import cqrs.infrastructure.command.SynchronousCommandBus
import cqrs.infrastructure.event.EventStoreBusInterceptor
import cqrs.infrastructure.event.SynchronousEventBus
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class ProviderEventsFetcherTest : ShouldSpec({
    val bus = SynchronousCommandBus()
    val eventStore = EventStoreBusInterceptor()
    val eventBus = SynchronousEventBus(listOf(eventStore))
    var events: List<ProviderEvent> = emptyList()
    fun setProviderResponse(events_: List<ProviderEvent>) {
        events = events_
    }
    val provider = object : EventsExternalProvider {
        override fun provide(): List<ProviderEvent> = events
    }
    val handler = ProviderEventsFetcher(provider, eventBus)
    bus.register(handler)

    should("find no events from 3rd party. thus, no event is emitted") {
        bus.handle<Unit>(FetchProviderEvents)
        eventStore.events shouldBe emptyList()
    }

    should("find some events, filtering offline sells") {
        val providerEvents = listOf(
            ProviderEventStub.random(sellMode = "online"),
            ProviderEventStub.random(sellMode = "online"),
            ProviderEventStub.random(sellMode = "offline"),
        )
        setProviderResponse(providerEvents)
        bus.handle<Unit>(FetchProviderEvents)
        eventStore.events shouldBe listOf(ProviderEventsFetched(providerEvents.dropLast(1)))
    }
})
