package cqrs.domain.event

interface EventBus {
    fun publish(domainEvent: DomainEvent)
    fun <E: DomainEvent> register(handler: EventHandler<E>)
}
