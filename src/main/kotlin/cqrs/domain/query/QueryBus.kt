package cqrs.domain.query

interface QueryBus {
    fun <R> handle(query: Query): R
    fun <Q: Query> register(handler: QueryHandler<Q, *>)
}
