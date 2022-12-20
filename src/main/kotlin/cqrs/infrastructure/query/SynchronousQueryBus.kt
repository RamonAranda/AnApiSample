package cqrs.infrastructure.query

import cqrs.domain.NoRegisteredHandlerException
import cqrs.domain.query.Query
import cqrs.domain.query.QueryBus
import cqrs.domain.query.QueryHandler
import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmErasure

class SynchronousQueryBus : QueryBus {
    private val handlers: MutableMap<KClass<out Query>, QueryHandler<out Query, *>> = mutableMapOf()

    override fun <R> handle(query: Query): R =
        (handlers[query::class] as? QueryHandler<Query, R>)
            ?.handle(query)
            ?: throw NoRegisteredHandlerException.forQuery(query)

    override fun <C : Query> register(handler: QueryHandler<C, *>) {
        val queryClass = handler::class.supertypes[0].arguments[0].type!!.jvmErasure as KClass<C>
        handlers[queryClass] = handler
    }
}
