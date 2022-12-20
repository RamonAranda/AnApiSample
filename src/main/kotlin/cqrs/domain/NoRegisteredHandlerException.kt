package cqrs.domain

import cqrs.domain.command.Command
import cqrs.domain.query.Query

class NoRegisteredHandlerException private constructor(message: String): RuntimeException(message) {
    companion object {
        fun forQuery(query: Query) =
            NoRegisteredHandlerException("No handler was registered for <${query.name()}>.")
        fun forCommand(command: Command) =
            NoRegisteredHandlerException("No handler was registered for <${command.name()}>.")
    }
}
private fun Query.name() = javaClass.kotlin.simpleName
private fun Command.name() = javaClass.kotlin.simpleName
