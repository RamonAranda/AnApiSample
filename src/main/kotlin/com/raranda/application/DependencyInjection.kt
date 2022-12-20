package com.raranda

import com.raranda.application.Configuration
import com.raranda.application.DomainModule
import com.raranda.domain.events.EventsDomainModule
import com.raranda.domain.providers.ProvidersDomainModule
import org.kodein.di.DI
import org.kodein.di.bindInstance
import org.kodein.di.direct
import org.kodein.di.instance

fun container(configuration: Configuration): DI =
    DI {
        bindInstance { configuration }
        import(CQRSModule())
        import(EventsDomainModule.module(configuration))
        import(ProvidersDomainModule.module(configuration))
    }.apply {
        register(EventsDomainModule)
        register(ProvidersDomainModule)
    }

private fun DI.register(module: DomainModule) {
    module.registerQueryHandlers(this)
    module.registerCommandHandlers(this)
    module.registerEventHandlers(this)
}

inline fun <reified T : Any> DI.get(tag: String? = null): T = direct.instance(tag)
