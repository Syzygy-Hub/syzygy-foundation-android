package com.syzygyhub.foundation.contracts.analytics

import com.syzygyhub.foundation.primitives.time.SyzygyTimestamp

/**
 * A discrete event to be recorded by an [AnalyticsProvider].
 *
 * @property name A short, dot-separated event identifier (e.g.
 *   `"user.signed_in"`).
 * @property properties Arbitrary key-value metadata associated with the
 *   event. Values are **[String]** rather than [Any] to guarantee safe
 *   serialization and Kotlin coroutine boundary crossing — no runtime type
 *   checks or reflection are required.
 * @property timestamp The moment the event occurred. Defaults to the
 *   current system time.
 */
data class AnalyticsEvent(
    val name: String,
    val properties: Map<String, String> = emptyMap(),
    val timestamp: SyzygyTimestamp = SyzygyTimestamp.now(),
)
