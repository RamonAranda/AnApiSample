package com.raranda.domain.providers.integration

import com.raranda.adapter.output.providers.EventsExternalProviderLocalStorage
import com.raranda.application.TestConfiguration
import com.raranda.domain.events.stub.InstantStub
import com.raranda.domain.providers.fetch.ProviderEvent
import com.raranda.domain.providers.fetch.ProviderZone
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class EventsExternalProviderHttpTest : ShouldSpec({
    should("return no events if 3rd party service is down") {
        val provider = EventsExternalProviderLocalStorage("")
        val events = provider.provide()
        events shouldBe emptyList()
    }

    should("get 3rd party service events") {
        val provider = EventsExternalProviderLocalStorage(TestConfiguration.providerXMLPath)
        val events = provider.provide()
        events shouldBe listOf(
            ProviderEvent(
                id = "291",
                sellMode = "online",
                title = "Camela en concierto",
                startDate = InstantStub.of("2021-06-30T21:00:00Z"),
                endDate = InstantStub.of("2021-06-30T21:30:00Z"),
                sellFrom = InstantStub.of("2020-07-01T00:00:00Z"),
                sellTo = InstantStub.of("2021-06-30T20:00:00Z"),
                soldOut = false,
                zones = listOf(
                    ProviderZone(
                        id = "40",
                        capacity = 200,
                        price = "20.00".toBigDecimal(),
                        name = "Platea",
                        numbered = true
                    ),
                    ProviderZone(
                        id = "38",
                        capacity = 0,
                        price = "15.00".toBigDecimal(),
                        name = "Grada 2",
                        numbered = false
                    ),
                    ProviderZone(
                        id = "30",
                        capacity = 80,
                        price = "30.00".toBigDecimal(),
                        name = "A28",
                        numbered = true
                    ),
                ),
            ),
            ProviderEvent(
                id = "1591",
                sellMode = "online",
                title = "Los Morancos",
                startDate = InstantStub.of("2021-07-31T20:00:00Z"),
                endDate = InstantStub.of("2021-07-31T21:00:00Z"),
                sellFrom = InstantStub.of("2021-06-26T00:00:00Z"),
                sellTo = InstantStub.of("2021-07-31T19:50:00Z"),
                soldOut = false,
                zones = listOf(
                    ProviderZone(
                        id = "186",
                        capacity = 0,
                        price = "75.00".toBigDecimal(),
                        name = "Amfiteatre",
                        numbered = true
                    ),
                    ProviderZone(
                        id = "186",
                        capacity = 12,
                        price = "65.00".toBigDecimal(),
                        name = "Amfiteatre",
                        numbered = false
                    ),
                ),
            ),
            ProviderEvent(
                id = "444",
                sellMode = "offline",
                title = "Tributo a Juanito Valderrama",
                startDate = InstantStub.of("2021-09-30T20:00:00Z"),
                endDate = InstantStub.of("2021-09-30T20:00:00Z"),
                sellFrom = InstantStub.of("2021-02-10T00:00:00Z"),
                sellTo = InstantStub.of("2021-09-30T19:50:00Z"),
                soldOut = false,
                zones = listOf(
                    ProviderZone(
                        id = "7",
                        capacity = 22,
                        price = "65.00".toBigDecimal(),
                        name = "Amfiteatre",
                        numbered = false
                    )
                ),
            )
        )
    }
})
