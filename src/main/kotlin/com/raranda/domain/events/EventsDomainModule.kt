package com.raranda.domain.events

import com.raranda.adapter.output.events.EventProjectionRepositoryInMemory
import com.raranda.application.Configuration
import com.raranda.application.DomainModule
import com.raranda.domain.events.projection.EventProjectionRepository
import com.raranda.domain.events.projection.get.EventsGetter
import com.raranda.domain.events.projection.update.ProjectEventsOnProviderEventsFetched
import com.raranda.get
import cqrs.domain.event.EventBus
import cqrs.domain.query.QueryBus
import org.kodein.di.DI
import org.kodein.di.bindInstance

object EventsDomainModule : DomainModule {
    override fun module(configuration: Configuration) = DI.Module("EventsDomainModule") {
        bindInstance<EventProjectionRepository> { EventProjectionRepositoryInMemory() }
    }

    override fun registerQueryHandlers(di: DI) {
        val queryBus: QueryBus = di.get()
        queryBus.register(EventsGetter(di.get()))
    }

    override fun registerCommandHandlers(di: DI) {
    }

    override fun registerEventHandlers(di: DI) {
        val eventBus: EventBus = di.get()
        eventBus.register(ProjectEventsOnProviderEventsFetched(di.get()))
    }
}
