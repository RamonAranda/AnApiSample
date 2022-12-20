package com.raranda.domain.events.acceptance

import com.raranda.common.getInstance
import com.raranda.common.givenRunningApi
import com.raranda.domain.events.EventId
import com.raranda.domain.events.EventTitle
import com.raranda.domain.events.Price
import com.raranda.domain.events.projection.EventProjectionRepository
import com.raranda.domain.events.projection.ProjectedEvent
import com.raranda.domain.events.stub.InstantStub
import com.raranda.domain.events.stub.ProjectedEventStub
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.ApplicationTestBuilder
import java.math.BigDecimal

class GetEventsAcceptanceTest : ShouldSpec({
    context("Get events") {
        should("return an empty list of events when no events are found") {
            givenRunningApi {
                val response = client.get("/search?starts_at=2022-11-22T00:00:00Z&ends_at=2022-11-22T00:00:00Z")
                response.status shouldBe HttpStatusCode.OK
                response.bodyAsText() shouldBe """
                    {
                      "data" : [ ],
                      "error" : null
                    }
                """.trimIndent()
            }
        }
        should("return a list of events") {
            givenRunningApi {
                val anEvent = ProjectedEventStub.random(
                    id = EventId("394034f3-767e-4a49-b4c7-6712ff7815e0"),
                    title = EventTitle("An awesome event"),
                    startDate = InstantStub.of("2022-11-22T00:00:00Z"),
                    endDate = InstantStub.of("2022-11-23T12:59:59Z"),
                    minPrice = Price(BigDecimal(10)),
                    maxPrice = Price(BigDecimal(20))
                )
                givenAnEventExists(anEvent)
                val response = client.get("/search?starts_at=2022-11-21T00:00:00Z&ends_at=2022-11-24T00:00:00Z")
                response.status shouldBe HttpStatusCode.OK
                response.bodyAsText() shouldBe """
                    {
                      "data" : [ {
                        "id" : "394034f3-767e-4a49-b4c7-6712ff7815e0",
                        "title" : "An awesome event",
                        "start_date" : "2022-11-22",
                        "start_time" : "00:00:00",
                        "end_date" : "2022-11-23",
                        "end_time" : "12:59:59",
                        "min_price" : 10,
                        "max_price" : 20
                      } ],
                      "error" : null
                    }
                """.trimIndent()
            }
        }
    }
    context("Malformed requests") {
        should("return 400 for a bad request when missing ends_at") {
            givenRunningApi {
                val response = client.get("/search?starts_at=\"\"")
                response.status shouldBe HttpStatusCode.BadRequest
                response.bodyAsText() shouldBe """
                    {
                      "data" : null,
                      "error" : {
                        "code" : 400,
                        "message" : "Missing parameters <ends_at>"
                      }
                    }
                """.trimIndent()
            }
        }
        should("return 400 for a bad request when missing starts_at") {
            givenRunningApi {
                val response = client.get("/search?ends_at=\"\"")
                response.status shouldBe HttpStatusCode.BadRequest
                response.bodyAsText() shouldBe """
                    {
                      "data" : null,
                      "error" : {
                        "code" : 400,
                        "message" : "Missing parameters <starts_at>"
                      }
                    }
                """.trimIndent()
            }
        }
        should("return 400 for a bad request when missing all parameters") {
            givenRunningApi {
                val response = client.get("/search")
                response.status shouldBe HttpStatusCode.BadRequest
                response.bodyAsText() shouldBe """
                    {
                      "data" : null,
                      "error" : {
                        "code" : 400,
                        "message" : "Missing parameters <starts_at, ends_at>"
                      }
                    }
                """.trimIndent()
            }
        }
        should("return 400 for a bad request when start date is malformed") {
            givenRunningApi {
                val response = client.get("/search?starts_at=2022-11&ends_at=2022-11-23T00:00:00Z")
                response.status shouldBe HttpStatusCode.BadRequest
                response.bodyAsText() shouldBe """
                    {
                      "data" : null,
                      "error" : {
                        "code" : 400,
                        "message" : "Malformed starts_at parameter"
                      }
                    }
                """.trimIndent()
            }
        }
        should("return 400 for a bad request when end date is malformed") {
            givenRunningApi {
                val response = client.get("/search?starts_at=2022-11-22T00:00:00Z&ends_at=T00:00:00Z")
                response.status shouldBe HttpStatusCode.BadRequest
                response.bodyAsText() shouldBe """
                    {
                      "data" : null,
                      "error" : {
                        "code" : 400,
                        "message" : "Malformed ends_at parameter"
                      }
                    }
                """.trimIndent()

            }
        }
    }
})

private fun ApplicationTestBuilder.givenAnEventExists(anEvent: ProjectedEvent) {
    val repository: EventProjectionRepository = getInstance()
    repository.save(listOf(anEvent))
}
