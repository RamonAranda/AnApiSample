package com.raranda

import com.raranda.adapter.input.configureRouting
import com.raranda.adapter.input.configureSerialization
import com.raranda.adapter.input.startWorker
import com.raranda.application.ApiConfiguration
import com.raranda.application.Configuration
import com.raranda.domain.providers.fetch.FetchProviderEvents
import cqrs.domain.command.CommandBus
import cqrs.domain.query.QueryBus
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.kodein.di.DI

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::bootstrap)
        .start(wait = true)
}

fun Application.bootstrap(container: DI = container(ApiConfiguration)) {
    val queryBus: QueryBus = container.get()
    configureSerialization()
    configureRouting(queryBus)
    configureWorkers(container)
}

fun Application.configureWorkers(container: DI) {
    val configuration: Configuration = container.get()
    val commandBus: CommandBus = container.get()
    if (configuration.providersWorkerEnabled) {
        startWorker(configuration.providersWorkerIntervalBetweenRuns) {
            commandBus.handle(FetchProviderEvents)
        }
    }
}
