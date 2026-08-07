package com.syzygyhub.foundation.contracts.analytics

/**
 * Contract for recording analytics events.
 *
 * Implementations are injected by higher-level modules and live outside
 * Foundation.
 */
interface AnalyticsProvider {
    /** Records [event] with the analytics back-end. */
    fun track(event: AnalyticsEvent)

    /**
     * Associates the current session with [userId] and optional [traits].
     *
     * [traits] values are [String] for serialization safety — see
     * [AnalyticsEvent.properties].
     */
    fun identify(
        userId: String,
        traits: Map<String, String>,
    )

    /** Clears the currently identified user and resets session state. */
    fun reset()
}
