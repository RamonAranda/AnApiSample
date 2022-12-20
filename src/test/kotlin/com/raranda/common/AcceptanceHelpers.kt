package com.raranda.common

import com.raranda.application.TestConfiguration
import com.raranda.bootstrap
import com.raranda.container
import com.raranda.get
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication

val ApplicationTestBuilder.diContainer by lazy { container(TestConfiguration) }

inline fun <reified R: Any> ApplicationTestBuilder.getInstance(tag: String? = null): R =
    diContainer.get(tag)

fun givenRunningApi(block: suspend ApplicationTestBuilder.() -> Unit) {
    testApplication {
        val container = diContainer
        application {
            bootstrap(container)
        }
        block()
    }
}
