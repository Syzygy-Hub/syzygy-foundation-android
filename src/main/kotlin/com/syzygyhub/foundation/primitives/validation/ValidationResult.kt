package com.syzygyhub.foundation.primitives.validation

/**
 * The outcome of applying a [ValidationRule] to a value.
 */
sealed class ValidationResult {
    /** The value passed all validation checks. */
    object Valid : ValidationResult()

    /**
     * The value failed one or more validation checks.
     *
     * @property messages Human-readable descriptions of each violation.
     */
    data class Invalid(val messages: List<String>) : ValidationResult()

    /** `true` if this result represents a passing validation. */
    val isValid: Boolean get() = this is Valid

    /**
     * The list of validation failure messages, or an empty list if [Valid].
     *
     * Note: this shadows the [Invalid.messages] property on the subclass.
     * Access via the sealed class reference for cross-branch use.
     */
    val errorMessages: List<String> get() = if (this is Invalid) this.messages else emptyList()
}
