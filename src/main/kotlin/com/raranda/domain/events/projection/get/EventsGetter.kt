package com.raranda.domain.events.projection.get

import com.raranda.domain.events.projection.EventProjectionRepository
import com.raranda.domain.events.projection.ProjectedEvent
import cqrs.domain.query.QueryHandler
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class EventsGetter(private val repository: EventProjectionRepository) :
    QueryHandler<GetEvents, GetEventsResponse> {

    override fun handle(query: GetEvents): GetEventsResponse =
        repository.findBy(query.startDate, query.endDate)
            .map { it.asResponse() }
            .let { GetEventsResponse(it) }

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE.withZone(ZoneId.of("UTC"))
    private val timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME.withZone(ZoneId.of("UTC"))

    private fun ProjectedEvent.asResponse(): EventResponse =
        EventResponse(
            id = id.value,
            title = title.value,
            startDate = dateFormatter.format(startDate),
            startTime = timeFormatter.format(startDate),
            endDate = dateFormatter.format(endDate),
            endTime = timeFormatter.format(endDate),
            minPrice = minPrice.value,
            maxPrice = maxPrice.value,
        )

}
