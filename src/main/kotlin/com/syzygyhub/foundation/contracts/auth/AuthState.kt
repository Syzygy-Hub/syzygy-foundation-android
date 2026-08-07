package com.syzygyhub.foundation.contracts.auth

/**
 * Represents the current authentication state held by an [AuthProvider].
 */
sealed class AuthState {
    /** No user is signed in. */
    object Unauthenticated : AuthState()

    /**
     * A user is signed in and their token is valid.
     *
     * @property authToken The active [AuthToken].
     */
    data class Authenticated(val authToken: AuthToken) : AuthState()

    /**
     * A user was signed in but their token has expired.
     *
     * @property authToken The expired [AuthToken] (may still carry a refresh
     *   token usable to obtain a new one).
     */
    data class Expired(val authToken: AuthToken) : AuthState()

    /** A token refresh is in progress. */
    object Refreshing : AuthState()

    /** `true` when the user is in the [Authenticated] state. */
    val isAuthenticated: Boolean get() = this is Authenticated

    /**
     * Returns the current [AuthToken] for [Authenticated] or [Expired]
     * states, `null` otherwise.
     */
    val token: AuthToken?
        get() =
            when (this) {
                is Authenticated -> authToken
                is Expired -> authToken
                else -> null
            }
}
