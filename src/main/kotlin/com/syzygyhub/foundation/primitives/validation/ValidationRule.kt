package com.syzygyhub.foundation.primitives.validation

/**
 * A single rule that can validate a value of type [T].
 *
 * Foundation defines no built-in rules; implementations live in
 * higher-level modules that understand domain constraints.
 */
interface ValidationRule<T> {
    /** Validates [value] and returns a [ValidationResult]. */
    fun validate(value: T): ValidationResult
}
