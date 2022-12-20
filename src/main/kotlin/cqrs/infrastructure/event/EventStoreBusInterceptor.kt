package cqrs.infrastructure.event

import cqrs.domain.event.DomainEvent

class EventStoreBusInterceptor {
    private val eventStore: MutableList<DomainEvent> = mutableListOf()
    fun on(event: DomainEvent) {
        eventStore.add(event)
    }

    val events: List<DomainEvent>
        get() = eventStore.toList()
}
