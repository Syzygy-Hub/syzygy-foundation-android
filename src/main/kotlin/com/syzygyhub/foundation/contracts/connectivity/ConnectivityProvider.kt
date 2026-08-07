package com.syzygyhub.foundation.contracts.connectivity

import kotlinx.coroutines.flow.StateFlow

/**
 * Contract for observing network reachability.
 *
 * [state] is a [StateFlow] so that UI and service layers can react to
 * connectivity changes without polling.
 */
interface ConnectivityProvider {
    /**
     * A hot stream of the current [ConnectivityState].
     *
     * Collectors always receive the latest state immediately upon collection
     * and are notified on every subsequent change.
     */
    val state: StateFlow<ConnectivityState>

    /**
     * A synchronous snapshot of whether the device is currently connected.
     *
     * Equivalent to `state.value.isConnected` but may be read without
     * launching a coroutine.
     */
    val isConnected: Boolean
}
