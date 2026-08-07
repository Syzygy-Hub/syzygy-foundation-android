package com.syzygyhub.foundation.sharedtypes

/**
 * Static metadata about the host application's binary.
 *
 * Foundation does not read from platform APIs (e.g. `PackageInfo`). Callers
 * construct this from whatever platform source is appropriate and inject it
 * via [SyzygyConfiguration].
 *
 * @property appName Human-readable application name.
 * @property bundleId Reverse-DNS application identifier
 *   (e.g. `"com.example.myapp"`).
 * @property buildNumber Build counter or CI build identifier.
 * @property version Human-readable version string (e.g. `"1.2.3"`).
 */
data class SyzygyBuildInfo(
    val appName: String,
    val bundleId: String,
    val buildNumber: String,
    val version: String,
)
