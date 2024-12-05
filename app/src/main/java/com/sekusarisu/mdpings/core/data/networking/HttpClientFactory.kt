package com.sekusarisu.mdpings.core.data.networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

object HttpClientFactory {

    @OptIn(ExperimentalSerializationApi::class)
    fun create(engine: HttpClientEngine): HttpClient {
        return HttpClient(engine) {
            install(HttpCookies) {
                storage = AcceptAllCookiesStorage()
            }
            install(WebSockets) {
                pingIntervalMillis = 20_000
            }
            install(HttpRequestRetry) {
                maxRetries = 3
                retryOnExceptionIf { request, cause ->
                    cause is HttpRequestTimeoutException
                }
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = Logger.ANDROID
            }
            install(ContentNegotiation) {
                json(
                    json = Json {
                        explicitNulls = false
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }
    }
}