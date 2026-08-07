package com.syzygyhub.foundation.primitives.time

import java.time.Instant

/**
 * An instant in time represented as milliseconds since the Unix epoch (UTC).
 *
 * Foundation does not depend on platform date/time APIs beyond
 * [java.time.Instant]; all platform-specific time concerns belong in
 * higher-level modules.
 */
data class SyzygyTimestamp(val millisecondsSinceEpoch: Long) : Comparable<SyzygyTimestamp> {
    /** Seconds since the Unix epoch (floating-point). */
    val secondsSinceEpoch: Double get() = millisecondsSinceEpoch / 1000.0

    /** Converts this timestamp to a [java.time.Instant]. */
    fun toInstant(): Instant = Instant.ofEpochMilli(millisecondsSinceEpoch)

    override fun compareTo(other: SyzygyTimestamp): Int = millisecondsSinceEpoch.compareTo(other.millisecondsSinceEpoch)

    companion object {
        /** Returns the current system time as a [SyzygyTimestamp]. */
        fun now(): SyzygyTimestamp = SyzygyTimestamp(System.currentTimeMillis())
    }
}
