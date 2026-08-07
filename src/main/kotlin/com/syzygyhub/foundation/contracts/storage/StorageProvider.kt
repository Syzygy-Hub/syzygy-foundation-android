package com.syzygyhub.foundation.contracts.storage

/**
 * Contract for key-value persistent storage.
 *
 * Android JVM interfaces cannot declare reified generic functions, so each
 * operation accepts explicit [serializer] / [deserializer] lambdas. This
 * mirrors the pattern used in idiomatic Android DataStore / SharedPreferences
 * wrappers and avoids unsafe reflection-based type tokens.
 */
interface StorageProvider {
    /**
     * Retrieves the value associated with [key], using [deserializer] to
     * convert the stored string representation, or returns `null` if no
     * value is stored.
     */
    fun <T : Any> get(
        key: StorageKey<T>,
        deserializer: (String) -> T,
    ): T?

    /**
     * Stores [value] under [key], using [serializer] to convert the value
     * to its string representation.
     */
    fun <T : Any> set(
        value: T,
        key: StorageKey<T>,
        serializer: (T) -> String,
    )

    /** Removes any value stored under [key]. */
    fun <T : Any> remove(key: StorageKey<T>)

    /** Removes all values from the store. */
    fun clear()
}
