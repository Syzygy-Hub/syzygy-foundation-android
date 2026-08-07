package com.syzygyhub.foundation.contracts.auth

import kotlinx.coroutines.flow.StateFlow

/**
 * Contract for managing user authentication state.
 *
 * [state] is a [StateFlow] so that UI and service layers can collect
 * authentication changes reactively without polling.
 */
interface AuthProvider {
    /**
     * A hot stream of the current [AuthState].
     *
     * Collectors always receive the latest state immediately upon collection
     * and are notified on every subsequent change.
     */
    val state: StateFlow<AuthState>

    /**
     * Stores [token] and transitions [state] to [AuthState.Authenticated].
     *
     * This is a synchronous write — the state update is visible before the
     * call returns.
     */
    fun authenticate(token: AuthToken)

    /**
     * Attempts to obtain a fresh [AuthToken] using the current refresh token.
     *
     * Suspends while the refresh is in progress. Transitions [state] to
     * [AuthState.Refreshing] during the operation and to
     * [AuthState.Authenticated] on success.
     *
     * @throws Exception if the refresh fails.
     */
    suspend fun refresh(): AuthToken

    /**
     * Clears all stored credentials and transitions [state] to
     * [AuthState.Unauthenticated].
     */
    fun signOut()
}
