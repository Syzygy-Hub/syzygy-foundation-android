package com.syzygyhub.foundation.sharedtypes

sealed class SyzygyFoundationError(message: String, cause: Throwable? = null) : Exception(message, cause) {
    class Network(cause: Throwable? = null) : SyzygyFoundationError("Network error", cause)

    class Authentication(cause: Throwable? = null) : SyzygyFoundationError("Authentication error", cause)

    object NotFound : SyzygyFoundationError("Not found")

    object Timeout : SyzygyFoundationError("Timeout")

    object Cancelled : SyzygyFoundationError("Cancelled")

    class Unknown(cause: Throwable? = null) : SyzygyFoundationError("Unknown error", cause)
}
