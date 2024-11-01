package com.example.mdpings.core.data.networking

fun constructUrl(baseURL: String, url: String): String {
    return when {
        url.contains(baseURL) -> url
        url.startsWith("/") -> baseURL + url.drop(1)
        else -> baseURL + url
    }
}