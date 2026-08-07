package com.syzygyhub.foundation.contracts.logging

/**
 * Contract for structured logging.
 *
 * Default interface methods provide convenience wrappers around the single
 * abstract [log] method so that implementations only need to override [log].
 */
interface LoggerProtocol {
    /** Records [entry] to the underlying log sink. */
    fun log(entry: LogEntry)

    /** Records a [LogLevel.DEBUG] message. */
    fun debug(
        message: String,
        metadata: Map<String, String> = emptyMap(),
    ) {
        log(LogEntry(LogLevel.DEBUG, message, com.syzygyhub.foundation.primitives.time.SyzygyTimestamp.now(), metadata))
    }

    /** Records a [LogLevel.INFO] message. */
    fun info(
        message: String,
        metadata: Map<String, String> = emptyMap(),
    ) {
        log(LogEntry(LogLevel.INFO, message, com.syzygyhub.foundation.primitives.time.SyzygyTimestamp.now(), metadata))
    }

    /** Records a [LogLevel.WARNING] message. */
    fun warning(
        message: String,
        metadata: Map<String, String> = emptyMap(),
    ) {
        log(LogEntry(LogLevel.WARNING, message, com.syzygyhub.foundation.primitives.time.SyzygyTimestamp.now(), metadata))
    }

    /** Records a [LogLevel.ERROR] message with an optional [Throwable]. */
    fun error(
        message: String,
        error: Throwable? = null,
        metadata: Map<String, String> = emptyMap(),
    ) {
        log(
            LogEntry(
                LogLevel.ERROR,
                message,
                com.syzygyhub.foundation.primitives.time.SyzygyTimestamp.now(),
                metadata,
                error,
            ),
        )
    }

    /** Records a [LogLevel.CRITICAL] message with an optional [Throwable]. */
    fun critical(
        message: String,
        error: Throwable? = null,
        metadata: Map<String, String> = emptyMap(),
    ) {
        log(
            LogEntry(
                LogLevel.CRITICAL,
                message,
                com.syzygyhub.foundation.primitives.time.SyzygyTimestamp.now(),
                metadata,
                error,
            ),
        )
    }
}
