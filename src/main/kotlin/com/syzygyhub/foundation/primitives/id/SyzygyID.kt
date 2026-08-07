package com.syzygyhub.foundation.primitives.id

/**
 * A type-safe, generic identifier wrapping a raw string value.
 *
 * The phantom type parameter [T] prevents accidental cross-domain ID comparisons
 * at compile time while preserving a simple String representation at runtime.
 */
data class SyzygyID<T>(val rawValue: String) : Comparable<SyzygyID<T>> {
    override fun compareTo(other: SyzygyID<T>): Int = rawValue.compareTo(other.rawValue)

    override fun toString(): String = rawValue

    companion object {
        /** Generates a new random UUID-backed [SyzygyID] for type [T]. */
        inline fun <reified T> generate(): SyzygyID<T> = SyzygyID(java.util.UUID.randomUUID().toString())
    }
}
