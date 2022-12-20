package com.raranda.domain.providers.fetch

import java.math.BigDecimal
import java.time.Instant

data class ProviderZone(
    val id: String,
    val capacity: Int,
    val price: BigDecimal,
    val name: String,
    val numbered: Boolean
)

data class ProviderEvent(
    val id: String,
    val sellMode: String,
    val title: String,
    val startDate: Instant,
    val endDate: Instant,
    val sellFrom: Instant,
    val sellTo: Instant,
    val soldOut: Boolean,
    val zones: List<ProviderZone>
)
