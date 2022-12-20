package com.raranda.domain.providers

import com.raranda.domain.providers.fetch.ProviderEvent

interface EventsExternalProvider {
    fun provide(): List<ProviderEvent>
}
