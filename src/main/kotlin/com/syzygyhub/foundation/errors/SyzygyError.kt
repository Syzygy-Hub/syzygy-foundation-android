package com.syzygyhub.foundation.errors

/**
 * Contract for all typed errors produced by Syzygy modules.
 *
 * Concrete implementations should extend [Throwable] (typically [Exception])
 * so that [SyzygyError] instances can be thrown and caught using standard
 * Kotlin/JVM exception handling while also carrying structured metadata.
 * Kotlin interfaces cannot extend classes directly; the [Throwable] constraint
 * is documented by convention rather than enforced at the type level.
 *
 * Foundation defines only the contract; concrete error types live in
 * higher-level modules that understand domain failure modes.
 */
interface SyzygyError {
    /** Machine-readable identifier for this error. */
    val code: SyzygyErrorCode

    /** Human-readable description of this error. */
    val message: String

    /** The operational impact of this error. */
    val severity: SyzygyErrorSeverity

    /** The lower-level exception that caused this error, if any. */
    val underlyingError: Throwable?
}
