package com.raranda.domain.providers.stub

import com.raranda.domain.events.stub.InstantStub
import com.raranda.domain.providers.fetch.ProviderEvent
import com.raranda.domain.providers.fetch.ProviderZone
import java.time.Instant
import java.util.UUID
import kotlin.random.Random

object ProviderEventStub {
    fun random(
        id: String = UUID.randomUUID().toString(),
        sellMode: String = listOf("online", "offline").random(),
        title: String = "title $id",
        startDate: Instant = InstantStub.of("2020-02-01T00:00:00Z"),
        endDate: Instant = InstantStub.of("2020-03-01T00:00:00Z"),
        sellFrom: Instant = InstantStub.of("2020-01-01T00:00:00Z"),
        sellTo: Instant = InstantStub.of("2020-02-01T00:00:00Z"),
        soldOut: Boolean = Random.nextBoolean(),
        zones: List<ProviderZone> = listOf(ProviderZoneStub.random(), ProviderZoneStub.random())
    ) = ProviderEvent(
        id = id,
        sellMode = sellMode,
        title = title,
        startDate = startDate,
        endDate = endDate,
        sellFrom = sellFrom,
        sellTo = sellTo,
        soldOut = soldOut,
        zones = zones,
    )
}
