package cqrs.command

import cqrs.domain.event.DomainEvent
import cqrs.domain.event.EventHandler
import cqrs.infrastructure.event.SynchronousEventBus
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import kotlin.properties.Delegates


class SynchronousEventBusTest : ShouldSpec({

    data class SampleDomainEvent(val anInteger: Int) : DomainEvent

    val bus = SynchronousEventBus()

    should("handles an event") {
        var increasedValue by Delegates.notNull<Int>()
        var decreasedValue by Delegates.notNull<Int>()
        val aHandlerThatIncreasesTheGivenValue = object : EventHandler<SampleDomainEvent> {
            override fun on(event: SampleDomainEvent) = run { increasedValue = event.anInteger + 1 }
        }
        val aHandlerThatDecreasesTheGivenValue = object : EventHandler<SampleDomainEvent> {
            override fun on(event: SampleDomainEvent) = run { decreasedValue = event.anInteger - 1 }
        }
        bus.register(aHandlerThatIncreasesTheGivenValue)
        bus.register(aHandlerThatDecreasesTheGivenValue)
        val aDomainEvent = SampleDomainEvent(5)
        bus.publish(aDomainEvent)
        increasedValue shouldBe 6
        decreasedValue shouldBe 4
    }
    should("do nothing if the event has no handlers") {
        val anEventWithNoHandler = object : DomainEvent {}
        bus.publish(anEventWithNoHandler)
    }
})
