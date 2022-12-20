package com.raranda

import cqrs.domain.command.CommandBus
import cqrs.domain.event.EventBus
import cqrs.domain.query.QueryBus
import cqrs.infrastructure.command.SynchronousCommandBus
import cqrs.infrastructure.event.SynchronousEventBus
import cqrs.infrastructure.query.SynchronousQueryBus
import org.kodein.di.DI
import org.kodein.di.bindInstance

fun CQRSModule() = DI.Module("CQRSModule") {
    bindInstance<QueryBus> { SynchronousQueryBus() }
    bindInstance<CommandBus> { SynchronousCommandBus() }
    bindInstance<EventBus> { SynchronousEventBus() }
}
