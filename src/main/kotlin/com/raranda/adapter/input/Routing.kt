package com.raranda.adapter.input

import com.raranda.adapter.input.events.eventsRouting
import cqrs.domain.query.QueryBus
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting(queryBus: QueryBus) {
    routing {
        eventsRouting(queryBus)
    }
}
