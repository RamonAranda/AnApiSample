package cqrs.domain.command

interface CommandBus {
    fun <R> handle(command: Command): R
    fun <C: Command> register(handler: CommandHandler<C, *>)
}
