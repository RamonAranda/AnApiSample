package cqrs.query

import cqrs.domain.NoRegisteredHandlerException
import cqrs.domain.query.Query
import cqrs.domain.query.QueryHandler
import cqrs.infrastructure.query.SynchronousQueryBus
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe


class SynchronousQueryBusTest : ShouldSpec({

    data class SampleQuery(val anInteger: Int) : Query
    data class SampleResponse(val aResponse: String)

    val bus = SynchronousQueryBus()
    val expectedResponse = SampleResponse("a response")
    bus.register(object : QueryHandler<SampleQuery, SampleResponse> {
        override fun handle(query: SampleQuery): SampleResponse = expectedResponse
    })

    should("handle a query") {
        val query = SampleQuery(1)
        bus.handle<SampleResponse>(query) shouldBe expectedResponse
    }
    should("raise an exception when a query has no registered handler") {
        val aQueryWithNoHandler = object : Query {}
        shouldThrow<NoRegisteredHandlerException> { bus.handle<Any>(aQueryWithNoHandler) }
    }
})
