package com.raranda.application

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface Configuration {
    val providerXMLPath: String
    val providersWorkerEnabled: Boolean
    val providersWorkerIntervalBetweenRuns: Duration
}

object TestConfiguration : Configuration {
    override val providerXMLPath: String = "test.xml"
    override val providersWorkerEnabled: Boolean = false
    override val providersWorkerIntervalBetweenRuns: Duration = 0.seconds
}

object ApiConfiguration : Configuration {
    override val providerXMLPath: String = "prod.xml"
    override val providersWorkerEnabled: Boolean = true
    override val providersWorkerIntervalBetweenRuns: Duration = 60.seconds
}
