package com.syzygyhub.foundation.contracts.network

/**
 * A description of an outbound HTTP request.
 *
 * [body] uses [ByteArray] to remain encoding-agnostic; serialization/
 * deserialization is handled by higher-level modules.
 *
 * [equals] and [hashCode] are overridden to handle [ByteArray] structural
 * equality, which the default Kotlin `data class` implementation does not
 * provide.
 */
data class NetworkRequest(
    val url: String,
    val method: NetworkMethod,
    val headers: Map<String, String> = emptyMap(),
    val body: ByteArray? = null,
    val timeoutSeconds: Double = 30.0,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is NetworkRequest) return false
        return url == other.url &&
            method == other.method &&
            headers == other.headers &&
            (body == null && other.body == null || body != null && body.contentEquals(other.body ?: return false)) &&
            timeoutSeconds == other.timeoutSeconds
    }

    override fun hashCode(): Int {
        var result = url.hashCode()
        result = 31 * result + method.hashCode()
        result = 31 * result + headers.hashCode()
        result = 31 * result + (body?.contentHashCode() ?: 0)
        result = 31 * result + timeoutSeconds.hashCode()
        return result
    }
}
