package com.raranda.adapter.input.events

import com.raranda.adapter.input.HttpResponse
import com.raranda.domain.events.projection.get.GetEvents
import com.raranda.domain.events.projection.get.GetEventsResponse
import cqrs.domain.query.QueryBus
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import java.time.Instant

fun Routing.eventsRouting(queryBus: QueryBus) {
    get("/search") {
        val rawStartDate = call.parameters["starts_at"]
        val rawEndDate = call.parameters["ends_at"]
        if (rawStartDate == null || rawEndDate == null) {
            return@get call.handleMissingParameters(rawStartDate, rawEndDate)
        }
        val startDate = try {
            Instant.parse(rawStartDate)
        } catch (e: Exception) {
            return@get call.handleMalformedDate("starts_at")
        }
        val endDate = try {
            Instant.parse(rawEndDate)
        } catch (e: Exception) {
            return@get call.handleMalformedDate("ends_at")
        }
        val query = GetEvents(startDate, endDate)
        val response = queryBus.handle<GetEventsResponse>(query)
        call.respond(HttpStatusCode.OK, HttpResponse.ok(response.events))
    }
}

private suspend fun ApplicationCall.handleMissingParameters(startDate: String?, endDate: String?) {
    val missingParameters = listOfNotNull(
        if (startDate == null) "starts_at" else null,
        if (endDate == null) "ends_at" else null
    )
    val message = "Missing parameters <${missingParameters.joinToString(separator = ", ") { it }}>"
    respond(HttpStatusCode.BadRequest, HttpResponse.badRequest(message))
}

private suspend fun ApplicationCall.handleMalformedDate(name: String) {
    val message = "Malformed $name parameter"
    respond(HttpStatusCode.BadRequest, HttpResponse.badRequest(message))
}
