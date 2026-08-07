package com.syzygyhub.foundation.contracts.logging

/**
 * The importance of a log message.
 *
 * Levels are ordered by severity; [Comparable] ordering matches severity
 * ordering so that `level >= LogLevel.WARNING` expressions are natural.
 *
 * @property severity A numeric severity value that increases with
 *   importance.
 */
enum class LogLevel(val severity: Int) : Comparable<LogLevel> {
    DEBUG(0),
    INFO(1),
    WARNING(2),
    ERROR(3),
    CRITICAL(4),
}
