package com.raranda.adapter.input

import io.ktor.server.application.Application
import kotlin.time.Duration
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun Application.startWorker(delay: Duration, block: suspend () -> Unit) = launch {
    while (true) {
        block()
        delay(delay)
    }
}
