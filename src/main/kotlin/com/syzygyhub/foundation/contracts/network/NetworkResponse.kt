package com.syzygyhub.foundation.contracts.network

/**
 * The result of executing a [NetworkRequest] via a [NetworkClientProtocol].
 *
 * [data] is a raw [ByteArray]; decoding to a domain model is handled by
 * higher-level modules.
 *
 * [equals] and [hashCode] are overridden to handle [ByteArray] structural
 * equality, which the default Kotlin `data class` implementation does not
 * provide.
 */
data class NetworkResponse(
    val statusCode: Int,
    val data: ByteArray,
    val headers: Map<String, String>,
) {
    /** `true` for HTTP 2xx status codes. */
    val isSuccess: Boolean get() = statusCode in 200..299

    /** `true` for HTTP 4xx status codes. */
    val isClientError: Boolean get() = statusCode in 400..499

    /** `true` for HTTP 5xx status codes. */
    val isServerError: Boolean get() = statusCode in 500..599

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is NetworkResponse) return false
        return statusCode == other.statusCode &&
            data.contentEquals(other.data) &&
            headers == other.headers
    }

    override fun hashCode(): Int {
        var result = statusCode.hashCode()
        result = 31 * result + data.contentHashCode()
        result = 31 * result + headers.hashCode()
        return result
    }
}
