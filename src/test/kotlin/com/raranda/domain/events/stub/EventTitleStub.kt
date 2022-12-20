package com.raranda.domain.events.stub

import com.raranda.domain.events.EventTitle
import java.util.UUID

object EventTitleStub {
    fun random() = EventTitle(UUID.randomUUID().toString())
}
