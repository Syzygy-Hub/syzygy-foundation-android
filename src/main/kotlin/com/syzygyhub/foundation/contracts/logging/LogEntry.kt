package com.syzygyhub.foundation.contracts.logging

import com.syzygyhub.foundation.primitives.time.SyzygyTimestamp

/**
 * A single structured log record.
 *
 * [metadata] values are [String] rather than [Any] for serialization safety
 * and safe coroutine boundary crossing — mirrors the same decision made for
 * [AnalyticsEvent.properties].
 *
 * [error] is deliberately excluded from [equals] and [hashCode] because
 * [Throwable] does not implement value equality. Two entries with the same
 * level, message, timestamp, and metadata are considered equal regardless
 * of the attached exception.
 */
data class LogEntry(
    val level: LogLevel,
    val message: String,
    val timestamp: SyzygyTimestamp,
    val metadata: Map<String, String> = emptyMap(),
    /** Excluded from [equals]/[hashCode] — [Throwable] lacks value equality. */
    val error: Throwable? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LogEntry) return false
        return level == other.level &&
            message == other.message &&
            timestamp == other.timestamp &&
            metadata == other.metadata
    }

    override fun hashCode(): Int = java.util.Objects.hash(level, message, timestamp, metadata)
}
