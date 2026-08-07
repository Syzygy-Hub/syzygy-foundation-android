package com.syzygyhub.foundation.testing.fixtures

import com.syzygyhub.foundation.contracts.analytics.AnalyticsEvent
import com.syzygyhub.foundation.contracts.auth.AuthToken
import com.syzygyhub.foundation.contracts.logging.LogEntry
import com.syzygyhub.foundation.contracts.logging.LogLevel
import com.syzygyhub.foundation.contracts.network.NetworkMethod
import com.syzygyhub.foundation.contracts.network.NetworkRequest
import com.syzygyhub.foundation.contracts.network.NetworkResponse
import com.syzygyhub.foundation.primitives.time.SyzygyTimestamp
import com.syzygyhub.foundation.sharedtypes.SyzygyVersion

/**
 * A marker interface that a test-fixture factory can implement for a type [T].
 *
 * Prefer [Fixtures] for concrete fixture construction; this interface is
 * available for custom fixture factories that need to satisfy a common
 * contract.
 */
interface FixtureProvider<T> {
    /** Returns a pre-populated fixture instance of [T]. */
    fun fixture(): T
}

/**
 * Central registry of pre-populated fixture values for Foundation types.
 *
 * Use these in unit tests wherever a real value is needed but the specific
 * content is not under test.
 *
 * Kotlin `data class` definitions do not auto-generate companion objects, so
 * fixture factories are collected here rather than as extension functions on
 * `Companion` objects.
 */
object Fixtures {
    /** Returns a fixture [AuthToken] with static access and refresh tokens. */
    fun authToken(): AuthToken =
        AuthToken(
            accessToken = "fixture-access-token",
            refreshToken = "fixture-refresh-token",
        )

    /** Returns a fixture [NetworkRequest] using GET to a placeholder URL. */
    fun networkRequest(): NetworkRequest =
        NetworkRequest(
            url = "https://fixture.syzygy.dev/api",
            method = NetworkMethod.GET,
        )

    /** Returns a fixture [NetworkResponse] with a 200 status and empty body. */
    fun networkResponse(): NetworkResponse =
        NetworkResponse(
            statusCode = 200,
            data = ByteArray(0),
            headers = emptyMap(),
        )

    /** Returns a fixture [AnalyticsEvent] named `"test_event"`. */
    fun analyticsEvent(): AnalyticsEvent = AnalyticsEvent(name = "test_event")

    /** Returns a fixture [LogEntry] at [LogLevel.INFO] with epoch timestamp. */
    fun logEntry(): LogEntry =
        LogEntry(
            level = LogLevel.INFO,
            message = "fixture log message",
            timestamp = SyzygyTimestamp(0),
        )

    /** Returns a fixture [SyzygyVersion] representing `1.0.0`. */
    fun syzygyVersion(): SyzygyVersion = SyzygyVersion(1, 0, 0)
}
