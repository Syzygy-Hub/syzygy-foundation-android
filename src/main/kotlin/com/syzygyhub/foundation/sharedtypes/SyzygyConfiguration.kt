package com.syzygyhub.foundation.sharedtypes

/**
 * Top-level application configuration injected into every Syzygy module.
 *
 * Foundation defines only the contract; concrete implementations live in the
 * host application or an integration layer.
 */
interface SyzygyConfiguration {
    /** The active deployment environment. */
    val environment: SyzygyEnvironment

    /** The root URL of the Syzygy API (no trailing slash). */
    val baseURL: String

    /** Metadata about the host application's binary. */
    val buildInfo: SyzygyBuildInfo

    /** The Syzygy SDK version in use. */
    val version: SyzygyVersion
}
