package cqrs.infrastructure.command

import cqrs.domain.NoRegisteredHandlerException
import cqrs.domain.command.Command
import cqrs.domain.command.CommandBus
import cqrs.domain.command.CommandHandler
import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmErasure

class SynchronousCommandBus : CommandBus {
    private val handlers: MutableMap<KClass<out Command>, CommandHandler<out Command, *>> = mutableMapOf()

    override fun <R> handle(command: Command): R =
        (handlers[command::class] as? CommandHandler<Command, R>)
            ?.handle(command)
            ?: throw NoRegisteredHandlerException.forCommand(command)

    override fun <C : Command> register(handler: CommandHandler<C, *>) {
        val commandClass = handler::class.supertypes[0].arguments[0].type!!.jvmErasure as KClass<C>
        handlers[commandClass] = handler
    }
}
