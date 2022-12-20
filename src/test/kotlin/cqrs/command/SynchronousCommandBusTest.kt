package cqrs.command

import cqrs.domain.NoRegisteredHandlerException
import cqrs.domain.command.Command
import cqrs.domain.command.CommandHandler
import cqrs.infrastructure.command.SynchronousCommandBus
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe


class SynchronousCommandBusTest : ShouldSpec({

    data class SampleCommand(val anInteger: Int) : Command

    val bus = SynchronousCommandBus()
    bus.register(object : CommandHandler<SampleCommand, Int> {
        override fun handle(command: SampleCommand): Int = command.anInteger
    })

    should("handle a command") {
        val command = SampleCommand(1)
        bus.handle<Int>(command) shouldBe 1
    }
    should("raise an exception when a command has no registered handler") {
        val aCommandWithNoHandler = object : Command {}
        shouldThrow<NoRegisteredHandlerException> { bus.handle<Any>(aCommandWithNoHandler) }
    }
})
