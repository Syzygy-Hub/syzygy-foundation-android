package com.syzygyhub.foundation.errors

/**
 * An opaque, string-backed error code used to identify [SyzygyError] kinds
 * without coupling callers to a closed enum.
 *
 * Implemented as a `@JvmInline` value class so that it compiles to a plain
 * [String] on the JVM bytecode level — zero allocation overhead compared to
 * a wrapper object.
 */
@JvmInline
value class SyzygyErrorCode(val rawValue: String) {
    companion object {
        val unknown = SyzygyErrorCode("unknown")
        val cancelled = SyzygyErrorCode("cancelled")
        val timeout = SyzygyErrorCode("timeout")
        val unauthenticated = SyzygyErrorCode("unauthenticated")
        val forbidden = SyzygyErrorCode("forbidden")
        val notFound = SyzygyErrorCode("not_found")
        val serverError = SyzygyErrorCode("server_error")
        val networkUnavailable = SyzygyErrorCode("network_unavailable")
        val decodingFailed = SyzygyErrorCode("decoding_failed")
        val encodingFailed = SyzygyErrorCode("encoding_failed")
    }
}
