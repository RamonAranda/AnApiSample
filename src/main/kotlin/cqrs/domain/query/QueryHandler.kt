package cqrs.domain.query

interface QueryHandler<Q: Query, R> {
    fun handle(query: Q): R
}
