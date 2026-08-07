package com.syzygyhub.foundation.contracts.storage

/**
 * A typed key that identifies a value in a [StorageProvider].
 *
 * The phantom type parameter [T] lets call sites remain type-safe without
 * requiring reflection. Because Android JVM generics are erased at runtime,
 * [StorageProvider] accepts explicit serializer/deserializer lambdas at each
 * call site rather than relying on reified generics (which are unavailable
 * in interface definitions).
 *
 * @property identifier The string key used to look up the value in the
 *   underlying store.
 * @property defaultValue Optional value returned when no stored value exists.
 */
data class StorageKey<T>(
    val identifier: String,
    val defaultValue: T? = null,
)
