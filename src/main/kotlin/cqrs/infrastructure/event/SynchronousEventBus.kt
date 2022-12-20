package cqrs.infrastructure.event

import cqrs.domain.event.DomainEvent
import cqrs.domain.event.EventBus
import cqrs.domain.event.EventHandler
import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmErasure

class SynchronousEventBus(private val onPublishInterceptors: List<EventStoreBusInterceptor> = emptyList()) : EventBus {
    private val handlers: MutableMap<KClass<out DomainEvent>, MutableList<EventHandler<out DomainEvent>>> =
        mutableMapOf()

    override fun publish(domainEvent: DomainEvent) {
        onPublishInterceptors.forEach { interceptor -> interceptor.on(domainEvent) }
        handlers[domainEvent::class]
            ?.asSequence()
            ?.map { it as EventHandler<DomainEvent> }
            ?.forEach {
                try {
                    it.on(domainEvent)
                } catch (e: Exception) {
                    // We could implement retry policies here, redirect the event into a dead letter queue or just log what happened.
                    // But for this test, I just decided to ignore it as it is not needed at all for the test.
                }
            }
    }

    override fun <E : DomainEvent> register(handler: EventHandler<E>) {
        val eventClass = handler::class.supertypes[0].arguments[0].type!!.jvmErasure as KClass<E>
        handlers.computeIfAbsent(eventClass) { mutableListOf() }
        handlers[eventClass]!!.add(handler)
    }
}
