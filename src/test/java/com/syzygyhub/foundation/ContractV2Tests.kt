package com.syzygyhub.foundation

import com.syzygyhub.foundation.sharedtypes.SyzygyFoundationError
import com.syzygyhub.foundation.testing.mocks.MockAuthProvider
import com.syzygyhub.foundation.testing.mocks.MockConnectivityProvider
import com.syzygyhub.foundation.testing.mocks.MockNetworkClient
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

// ---------------------------------------------------------------------------
// MockNetworkClient v2.0.0 — dispose()
// ---------------------------------------------------------------------------

class MockNetworkClientV2Tests {
    @Test
    fun `dispose can be called without throwing`() {
        val client = MockNetworkClient()
        client.dispose()
    }
}

// ---------------------------------------------------------------------------
// MockConnectivityProvider v2.0.0 — dispose()
// ---------------------------------------------------------------------------

class MockConnectivityProviderV2Tests {
    @Test
    fun `dispose can be called without throwing`() {
        val provider = MockConnectivityProvider()
        provider.dispose()
    }
}

// ---------------------------------------------------------------------------
// MockAuthProvider v2.0.0 — canUseBiometric, authenticateWithBiometric, refreshToken
// ---------------------------------------------------------------------------

class MockAuthProviderV2Tests {
    @Test
    fun `canUseBiometric returns Boolean`() {
        val provider = MockAuthProvider()
        val result: Boolean = provider.canUseBiometric()
        assertNotNull(result)
    }

    @Test
    fun `authenticateWithBiometric returns Boolean`() =
        runBlocking {
            val provider = MockAuthProvider()
            val result: Boolean = provider.authenticateWithBiometric("reason")
            assertNotNull(result)
        }

    @Test
    fun `refreshToken returns Boolean`() =
        runBlocking {
            val provider = MockAuthProvider()
            val result: Boolean = provider.refreshToken()
            assertNotNull(result)
        }

    @Test
    fun `refreshToken returns refreshTokenResult value`() =
        runBlocking {
            val provider = MockAuthProvider()
            provider.refreshTokenResult = true
            val result = provider.refreshToken()
            assert(result)
        }
}

// ---------------------------------------------------------------------------
// SyzygyFoundationError — instantiation smoke test for all 6 cases
// ---------------------------------------------------------------------------

class SyzygyFoundationErrorTests {
    @Test
    fun `Network can be constructed`() {
        val e = SyzygyFoundationError.Network()
        assertNotNull(e)
    }

    @Test
    fun `Network with cause can be constructed`() {
        val e = SyzygyFoundationError.Network(cause = RuntimeException("net"))
        assertNotNull(e)
    }

    @Test
    fun `Authentication can be constructed`() {
        val e = SyzygyFoundationError.Authentication()
        assertNotNull(e)
    }

    @Test
    fun `NotFound can be constructed`() {
        val e = SyzygyFoundationError.NotFound
        assertNotNull(e)
    }

    @Test
    fun `Timeout can be constructed`() {
        val e = SyzygyFoundationError.Timeout
        assertNotNull(e)
    }

    @Test
    fun `Cancelled can be constructed`() {
        val e = SyzygyFoundationError.Cancelled
        assertNotNull(e)
    }

    @Test
    fun `Unknown can be constructed`() {
        val e = SyzygyFoundationError.Unknown()
        assertNotNull(e)
    }
}
