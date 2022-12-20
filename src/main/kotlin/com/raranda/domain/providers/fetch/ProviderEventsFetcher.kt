package com.raranda.domain.providers.fetch

import com.raranda.domain.providers.EventsExternalProvider
import cqrs.domain.command.CommandHandler
import cqrs.domain.event.EventBus

class ProviderEventsFetcher(
    private val provider: EventsExternalProvider,
    private val eventBus: EventBus
) : CommandHandler<FetchProviderEvents, Unit> {
    override fun handle(command: FetchProviderEvents) {
        val events = provider.provide().filter { it.sellMode == "online" }
        if (events.isNotEmpty()) {
            eventBus.publish(ProviderEventsFetched(events = events))
        }
    }
}
