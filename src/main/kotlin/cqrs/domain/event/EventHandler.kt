package cqrs.domain.event

interface EventHandler<E : DomainEvent> {
    fun on(event: E)
}
