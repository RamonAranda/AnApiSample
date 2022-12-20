package cqrs.domain.command

interface CommandHandler<C: Command, R> {
    fun handle(command: C): R
}
