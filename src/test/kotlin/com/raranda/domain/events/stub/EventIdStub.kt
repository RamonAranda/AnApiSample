package com.raranda.domain.events.stub

import com.raranda.domain.events.EventId
import java.util.UUID

object EventIdStub {
    fun random() = EventId(UUID.randomUUID().toString())
}
