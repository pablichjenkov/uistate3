package com.pablichj.incubator.amadeus

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.debug.*
import kotlinx.serialization.json.Json


internal val httpClient = HttpClient {
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
        /*filter { request ->
            request.url.host.contains("ktor.io")
        }*/
    }
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
}