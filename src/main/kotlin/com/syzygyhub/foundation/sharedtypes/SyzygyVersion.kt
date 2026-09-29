package com.syzygyhub.foundation.sharedtypes

/**
 * A semantic version number following the [SemVer 2.0](https://semver.org)
 * specification.
 *
 * Ordering is defined by [major], [minor], then [patch]. Pre-release labels
 * are not compared — two versions that differ only in [prerelease] are
 * considered equal by [compareTo].
 *
 * @property major Breaking-change version increment.
 * @property minor Backwards-compatible feature increment.
 * @property patch Backwards-compatible bug-fix increment.
 * @property prerelease Optional pre-release label (e.g. `"alpha.1"`).
 */
data class SyzygyVersion(
    val major: Int,
    val minor: Int,
    val patch: Int,
    val prerelease: String? = null,
) : Comparable<SyzygyVersion> {
    override fun compareTo(other: SyzygyVersion): Int {
        major.compareTo(other.major).takeIf { it != 0 }?.let { return it }
        minor.compareTo(other.minor).takeIf { it != 0 }?.let { return it }
        return patch.compareTo(other.patch)
    }

    /** Returns the version string, e.g. `"1.0.0"` or `"1.0.0-alpha.1"`. */
    override fun toString(): String =
        buildString {
            append("$major.$minor.$patch")
            prerelease?.let { append("-$it") }
        }

    companion object {
        /** The current Foundation library version. */
        val current: SyzygyVersion = SyzygyVersion(2, 0, 0)
    }
}
