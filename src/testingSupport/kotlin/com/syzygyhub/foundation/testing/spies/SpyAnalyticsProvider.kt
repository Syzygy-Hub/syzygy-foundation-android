package com.syzygyhub.foundation.testing.spies

import com.syzygyhub.foundation.contracts.analytics.AnalyticsEvent
import com.syzygyhub.foundation.contracts.analytics.AnalyticsProvider

/**
 * An [AnalyticsProvider] spy that records all calls made to it.
 *
 * Unlike a mock that returns pre-configured values, a spy merely observes
 * calls and makes them available for assertion after the fact.
 */
class SpyAnalyticsProvider : AnalyticsProvider {
    /** All events passed to [track], in order. */
    val trackedEvents: MutableList<AnalyticsEvent> = mutableListOf()

    /** All (userId, traits) pairs passed to [identify], in order. */
    val identifiedUsers: MutableList<Pair<String, Map<String, String>>> = mutableListOf()

    /** Number of times [reset] has been called. */
    var resetCallCount: Int = 0
        private set

    override fun track(event: AnalyticsEvent) {
        trackedEvents.add(event)
    }

    override fun identify(
        userId: String,
        traits: Map<String, String>,
    ) {
        identifiedUsers.add(userId to traits)
    }

    override fun reset() {
        resetCallCount++
    }

    /** Returns all tracked events whose [AnalyticsEvent.name] matches [named]. */
    fun events(named: String): List<AnalyticsEvent> = trackedEvents.filter { it.name == named }
}
