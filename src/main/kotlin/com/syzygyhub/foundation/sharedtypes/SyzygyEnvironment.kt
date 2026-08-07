package com.syzygyhub.foundation.sharedtypes

/**
 * The deployment environment in which the application is running.
 */
enum class SyzygyEnvironment {
    /** Local developer build with verbose diagnostics enabled. */
    DEBUG,

    /** Pre-production environment for QA and integration testing. */
    STAGING,

    /** Live environment serving real users. */
    PRODUCTION,

    ;

    /** `true` when this environment is [DEBUG]. */
    val isDebug: Boolean get() = this == DEBUG

    /** `true` when this environment is [PRODUCTION]. */
    val isProduction: Boolean get() = this == PRODUCTION

    /** Returns the lowercase name of this environment (e.g. `"debug"`). */
    override fun toString(): String = name.lowercase()
}
