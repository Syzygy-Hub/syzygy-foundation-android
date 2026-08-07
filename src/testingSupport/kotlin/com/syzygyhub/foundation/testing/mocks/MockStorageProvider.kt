package com.syzygyhub.foundation.testing.mocks

import com.syzygyhub.foundation.contracts.storage.StorageKey
import com.syzygyhub.foundation.contracts.storage.StorageProvider

/**
 * An in-memory [StorageProvider] for use in unit tests.
 *
 * All stored values are kept as [String] in a [MutableMap] and are
 * immediately visible after each write — no threading or batching.
 */
class MockStorageProvider : StorageProvider {
    /** The underlying in-memory string store. Accessible for assertions. */
    val storage: MutableMap<String, String> = mutableMapOf()

    override fun <T : Any> get(
        key: StorageKey<T>,
        deserializer: (String) -> T,
    ): T? = storage[key.identifier]?.let(deserializer)

    override fun <T : Any> set(
        value: T,
        key: StorageKey<T>,
        serializer: (T) -> String,
    ) {
        storage[key.identifier] = serializer(value)
    }

    override fun <T : Any> remove(key: StorageKey<T>) {
        storage.remove(key.identifier)
    }

    override fun clear() {
        storage.clear()
    }
}
