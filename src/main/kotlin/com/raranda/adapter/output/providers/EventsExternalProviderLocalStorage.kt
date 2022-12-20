package com.raranda.adapter.output.providers

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.raranda.domain.providers.EventsExternalProvider
import com.raranda.domain.providers.fetch.ProviderEvent
import com.raranda.domain.providers.fetch.ProviderZone
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import java.math.BigDecimal
import java.time.Instant
import java.io.File

private data class RawProviderZone(
    @JacksonXmlProperty(localName = "zone_id")
    val id: String,
    @JacksonXmlProperty(localName = "capacity")
    val capacity: Int,
    @JacksonXmlProperty(localName = "price")
    val price: BigDecimal,
    @JacksonXmlProperty(localName = "name")
    val name: String,
    @JacksonXmlProperty(localName = "numbered")
    val numbered: Boolean
)

private data class RawProviderEvent(
    @JacksonXmlProperty(localName = "event_id")
    val id: String,
    @JacksonXmlProperty(localName = "event_start_date")
    val startDate: String,
    @JacksonXmlProperty(localName = "event_end_date")
    val endDate: String,
    @JacksonXmlProperty(localName = "sell_from")
    val sellFrom: String,
    @JacksonXmlProperty(localName = "sell_to")
    val sellTo: String,
    @JacksonXmlProperty(localName = "sold_out")
    val soldOut: Boolean,
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "zone")
    val zones: List<RawProviderZone>
)

private data class RawProviderBaseEvent(
    @JacksonXmlProperty(localName = "base_event_id")
    val baseEventId: String,
    @JacksonXmlProperty(localName = "sell_mode")
    val sellMode: String,
    @JacksonXmlProperty(localName = "title")
    val title: String,
    @JacksonXmlProperty(localName = "event")
    val event: RawProviderEvent
)

@JacksonXmlRootElement(namespace = "eventList")
private data class ProviderEventResponse(
    @JacksonXmlProperty(localName = "output")
    val output: List<RawProviderBaseEvent>
)

class EventsExternalProviderLocalStorage(private val path: String) : EventsExternalProvider {
    private val client = HttpClient(CIO) {
        install(Logging) {
            level = LogLevel.INFO
        }
    }
    private val xmlMapper = XmlMapper()
        .registerModule(JacksonXmlModule())
        .registerKotlinModule()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)

    override fun provide(): List<ProviderEvent> {
        return try {
            val fileUri = this::class.java.classLoader.getResource(path)!!.toURI()
            val content = File(fileUri).readText(Charsets.UTF_8)
            val providerEvents = xmlMapper.readValue(content, ProviderEventResponse::class.java).output
            providerEvents.map { it.toProviderEvent() }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

private fun RawProviderBaseEvent.toProviderEvent() =
    ProviderEvent(
        // As I do not have the full product requirements, I will assume that the baseEventId is the id of the event.
        id = baseEventId,
        sellMode = sellMode,
        title = title,
        startDate = Instant.parse(event.startDate.fixDateIssues()),
        endDate = Instant.parse(event.endDate.fixDateIssues()),
        sellFrom = Instant.parse(event.sellFrom.fixDateIssues()),
        sellTo = Instant.parse(event.sellTo.fixDateIssues()),
        soldOut = event.soldOut,
        zones = event.zones.map { it.toProviderZone() }
    )

private fun RawProviderZone.toProviderZone() =
    ProviderZone(
        id = id,
        capacity = capacity,
        price = price,
        name = name,
        numbered = numbered
    )

private fun String.fixDateIssues() =
    this.fixTimestampFormat()
        .fixSeptemberLastDay()

// Provided timestamps does not have the trailing Z, so we add it.
private fun String.fixTimestampFormat() = if (endsWith("Z")) this else "${this}Z"

// Some given examples have a wrong date format, as september does not have 31 days.
// Thus, I decided to fix the date issues here as an anti corruption layer from the client.
private fun String.fixSeptemberLastDay() = this.replace("09-31", "09-30")
