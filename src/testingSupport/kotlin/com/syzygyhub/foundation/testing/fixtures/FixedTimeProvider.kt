package com.syzygyhub.foundation.testing.fixtures

import com.syzygyhub.foundation.primitives.time.SyzygyDuration
import com.syzygyhub.foundation.primitives.time.SyzygyTimestamp
import com.syzygyhub.foundation.primitives.time.TimeProvider

/**
 * A deterministic [TimeProvider] that returns a fixed timestamp.
 *
 * Use in tests that depend on the current time to avoid flakiness caused by
 * the real system clock.
 *
 * @property fixedTime The timestamp returned by [now]. Can be mutated
 *   between assertions to simulate the passage of time.
 */
class FixedTimeProvider(
    var fixedTime: SyzygyTimestamp = SyzygyTimestamp(0),
) : TimeProvider {
    override fun now(): SyzygyTimestamp = fixedTime

    override fun since(timestamp: SyzygyTimestamp): SyzygyDuration =
        SyzygyDuration(fixedTime.millisecondsSinceEpoch - timestamp.millisecondsSinceEpoch)
}
