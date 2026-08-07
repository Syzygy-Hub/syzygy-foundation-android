package com.syzygyhub.foundation.contracts.network

/**
 * The HTTP method used for a [NetworkRequest].
 *
 * @property value The standard HTTP method string (e.g. `"GET"`).
 */
enum class NetworkMethod(val value: String) {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE"),
    HEAD("HEAD"),
}
