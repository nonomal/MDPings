package com.sekusarisu.mdpings.core.data.networking

fun constructUrl(baseURL: String, url: String): String {
    val cleanBaseUrl = baseURL.trimEnd('/')
    val cleanUrl = url.trimStart('/')
    return "$cleanBaseUrl/$cleanUrl"
//    return when {
//        url.contains(baseURL) -> url
//        url.startsWith("/") -> baseURL + url.drop(1)
//        else -> baseURL + url
//    }
}

fun constructWSUrl(baseURL: String, url: String): String {
    // baseUrl = "https://abc.com" url = "/api/v1/ws/server"

    val cleanBaseUrl = baseURL.trimEnd('/')
    val cleanUrl = url.trimStart('/')
    val wsBaseUrl = cleanBaseUrl.replace("https://", "wss://")
    return "$wsBaseUrl/$cleanUrl"
}