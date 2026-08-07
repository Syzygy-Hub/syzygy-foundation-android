package com.syzygyhub.foundation.primitives.time

/**
 * A span of time measured in milliseconds.
 *
 * Companion factory methods mirror idiomatic usage found in the Syzygy iOS
 * SDK and keep consumer call sites readable.
 */
data class SyzygyDuration(val milliseconds: Long) : Comparable<SyzygyDuration> {
    /** Duration expressed in seconds. */
    val seconds: Double get() = milliseconds / 1000.0

    /** Duration expressed in minutes. */
    val minutes: Double get() = seconds / 60.0

    /** Duration expressed in hours. */
    val hours: Double get() = minutes / 60.0

    override fun compareTo(other: SyzygyDuration): Int = milliseconds.compareTo(other.milliseconds)

    companion object {
        /** Creates a [SyzygyDuration] from a millisecond count. */
        fun milliseconds(ms: Long): SyzygyDuration = SyzygyDuration(ms)

        /** Creates a [SyzygyDuration] from a second count. */
        fun seconds(s: Double): SyzygyDuration = SyzygyDuration((s * 1_000).toLong())

        /** Creates a [SyzygyDuration] from a minute count. */
        fun minutes(m: Double): SyzygyDuration = SyzygyDuration((m * 60 * 1_000).toLong())

        /** Creates a [SyzygyDuration] from an hour count. */
        fun hours(h: Double): SyzygyDuration = SyzygyDuration((h * 3_600 * 1_000).toLong())
    }
}
