package com.raranda.domain.providers.fetch

import cqrs.domain.event.DomainEvent

data class ProviderEventsFetched(val events: List<ProviderEvent>) : DomainEvent
