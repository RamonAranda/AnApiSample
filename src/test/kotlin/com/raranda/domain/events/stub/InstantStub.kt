package com.raranda.domain.events.stub

import java.time.Instant
import java.time.Period

object InstantStub {
    fun random() = Instant.now()
    fun of(dateTime: String) = Instant.parse(dateTime)
}

fun Instant.plusDays(days: Int) = this.plus(Period.ofDays(days))
