package com.syzygyhub.foundation.testing.mocks

import com.syzygyhub.foundation.contracts.auth.AuthProvider
import com.syzygyhub.foundation.contracts.auth.AuthState
import com.syzygyhub.foundation.contracts.auth.AuthToken
import com.syzygyhub.foundation.testing.fixtures.Fixtures
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * An [AuthProvider] implementation that allows tests to control state and
 * inspect call counts.
 */
class MockAuthProvider : AuthProvider {
    private val _state = MutableStateFlow<AuthState>(AuthState.Unauthenticated)

    override val state: StateFlow<AuthState> = _state

    /** Number of times [refresh] has been called. */
    var refreshCallCount: Int = 0
        private set

    /** Number of times [signOut] has been called. */
    var signOutCallCount: Int = 0
        private set

    /**
     * The [Result] returned by the next [refresh] call.
     *
     * Defaults to a successful fixture token. Set to a failure to simulate
     * refresh errors.
     */
    var refreshResult: Result<AuthToken> = Result.success(Fixtures.authToken())

    override fun authenticate(token: AuthToken) {
        _state.value = AuthState.Authenticated(authToken = token)
    }

    override suspend fun refresh(): AuthToken {
        refreshCallCount++
        return refreshResult.getOrThrow()
    }

    override fun signOut() {
        signOutCallCount++
        _state.value = AuthState.Unauthenticated
    }
}
