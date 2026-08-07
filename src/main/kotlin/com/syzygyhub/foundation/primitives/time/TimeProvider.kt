package com.syzygyhub.foundation.primitives.time

/**
 * Contract for obtaining the current time and measuring elapsed durations.
 *
 * Implementations are injected by higher-level modules so that code depending
 * on the clock can be tested deterministically (see [FixedTimeProvider] in
 * the testingSupport source set).
 */
interface TimeProvider {
    /** Returns the current instant. */
    fun now(): SyzygyTimestamp

    /** Returns the elapsed duration between [timestamp] and [now]. */
    fun since(timestamp: SyzygyTimestamp): SyzygyDuration
}
