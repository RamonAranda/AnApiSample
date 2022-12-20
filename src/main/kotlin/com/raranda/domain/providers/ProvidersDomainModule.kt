package com.raranda.domain.providers

import com.raranda.adapter.output.providers.EventsExternalProviderLocalStorage
import com.raranda.application.Configuration
import com.raranda.application.DomainModule
import com.raranda.domain.providers.fetch.ProviderEventsFetcher
import com.raranda.get
import cqrs.domain.command.CommandBus
import org.kodein.di.DI
import org.kodein.di.bindInstance

object ProvidersDomainModule : DomainModule {
    override fun module(configuration: Configuration) = DI.Module("ProvidersDomainModule") {
        bindInstance<EventsExternalProvider> { EventsExternalProviderLocalStorage(configuration.providerXMLPath) }
    }

    override fun registerQueryHandlers(di: DI) {
    }

    override fun registerCommandHandlers(di: DI) {
        val commandBus: CommandBus = di.get()
        commandBus.register(ProviderEventsFetcher(di.get(), di.get()))
    }

    override fun registerEventHandlers(di: DI) {
    }
}
