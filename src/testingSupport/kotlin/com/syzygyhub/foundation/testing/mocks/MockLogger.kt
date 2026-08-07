package com.syzygyhub.foundation.testing.mocks

import com.syzygyhub.foundation.contracts.logging.LogEntry
import com.syzygyhub.foundation.contracts.logging.LogLevel
import com.syzygyhub.foundation.contracts.logging.LoggerProtocol

/**
 * A [LoggerProtocol] implementation that records all log entries in memory.
 *
 * Use in unit tests to assert that specific messages were (or were not)
 * logged at particular levels.
 */
class MockLogger : LoggerProtocol {
    /** All entries that have been logged since the last [clear] call. */
    val entries: MutableList<LogEntry> = mutableListOf()

    override fun log(entry: LogEntry) {
        entries.add(entry)
    }

    /** Returns only the entries recorded at [forLevel]. */
    fun entries(forLevel: LogLevel): List<LogEntry> = entries.filter { it.level == forLevel }

    /** Clears all recorded entries. */
    fun clear() {
        entries.clear()
    }
}
