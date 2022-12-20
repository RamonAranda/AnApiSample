package com.raranda.application

import org.kodein.di.DI

interface DomainModule {
    fun module(configuration: Configuration): DI.Module
    fun registerQueryHandlers(di: DI)
    fun registerCommandHandlers(di: DI)
    fun registerEventHandlers(di: DI)
}
