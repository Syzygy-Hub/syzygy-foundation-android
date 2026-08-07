package com.syzygyhub.foundation.errors

/**
 * The impact level of a [SyzygyError].
 *
 * Ordered by severity; [Comparable] ordering matches severity ordering.
 *
 * @property level Numeric severity value that increases with impact.
 */
enum class SyzygyErrorSeverity(val level: Int) : Comparable<SyzygyErrorSeverity> {
    /** Informational — no action required. */
    INFO(0),

    /** Degraded experience — monitoring may be warranted. */
    WARNING(1),

    /** Operation failed — action required. */
    ERROR(2),

    /** System integrity is at risk — immediate action required. */
    CRITICAL(3),
}
