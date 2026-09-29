package com.syzygyhub.foundation.testing.mocks

import com.syzygyhub.foundation.contracts.connectivity.ConnectivityProvider
import com.syzygyhub.foundation.contracts.connectivity.ConnectivityState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * A [ConnectivityProvider] implementation backed by a [MutableStateFlow].
 *
 * Call [setState] to simulate connectivity changes in tests.
 */
class MockConnectivityProvider(
    initialState: ConnectivityState = ConnectivityState.CONNECTED,
) : ConnectivityProvider {
    private val _state = MutableStateFlow(initialState)

    override val state: StateFlow<ConnectivityState> = _state

    override val isConnected: Boolean get() = _state.value.isConnected

    /** Transitions [state] to [newState]. */
    fun setState(newState: ConnectivityState) {
        _state.value = newState
    }

    /** No-op implementation; provided for [ConnectivityProvider] conformance. */
    override fun dispose() {}
}
