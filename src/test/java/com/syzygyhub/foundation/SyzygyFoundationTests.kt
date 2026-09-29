package com.syzygyhub.foundation

import com.syzygyhub.foundation.contracts.analytics.AnalyticsEvent
import com.syzygyhub.foundation.contracts.auth.AuthState
import com.syzygyhub.foundation.contracts.auth.AuthToken
import com.syzygyhub.foundation.contracts.connectivity.ConnectivityState
import com.syzygyhub.foundation.contracts.logging.LogEntry
import com.syzygyhub.foundation.contracts.logging.LogLevel
import com.syzygyhub.foundation.contracts.network.NetworkMethod
import com.syzygyhub.foundation.contracts.network.NetworkRequest
import com.syzygyhub.foundation.contracts.network.NetworkResponse
import com.syzygyhub.foundation.contracts.storage.StorageKey
import com.syzygyhub.foundation.errors.SyzygyErrorCode
import com.syzygyhub.foundation.primitives.id.SyzygyID
import com.syzygyhub.foundation.primitives.pagination.Page
import com.syzygyhub.foundation.primitives.pagination.PaginationRequest
import com.syzygyhub.foundation.primitives.time.SyzygyDuration
import com.syzygyhub.foundation.primitives.time.SyzygyTimestamp
import com.syzygyhub.foundation.primitives.validation.ValidationResult
import com.syzygyhub.foundation.sharedtypes.SyzygyEnvironment
import com.syzygyhub.foundation.sharedtypes.SyzygyVersion
import com.syzygyhub.foundation.testing.fixtures.FixedTimeProvider
import com.syzygyhub.foundation.testing.mocks.MockAuthProvider
import com.syzygyhub.foundation.testing.mocks.MockConnectivityProvider
import com.syzygyhub.foundation.testing.mocks.MockLogger
import com.syzygyhub.foundation.testing.mocks.MockNetworkClient
import com.syzygyhub.foundation.testing.mocks.MockStorageProvider
import com.syzygyhub.foundation.testing.spies.SpyAnalyticsProvider
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

// ---------------------------------------------------------------------------
// Marker type used as a phantom type parameter where needed
// ---------------------------------------------------------------------------

private class User

// ---------------------------------------------------------------------------
// SyzygyID
// ---------------------------------------------------------------------------

class SyzygyIDTests {
    @Test
    fun `generate produces unique IDs across multiple calls`() {
        val a = SyzygyID.generate<User>()
        val b = SyzygyID.generate<User>()
        assertNotEquals(a, b)
    }

    @Test
    fun `rawValue round-trips correctly through init`() {
        val id = SyzygyID<User>("test-raw-value")
        assertEquals("test-raw-value", id.rawValue)
    }

    @Test
    fun `two IDs with same rawValue are equal`() {
        val a = SyzygyID<User>("same-id")
        val b = SyzygyID<User>("same-id")
        assertEquals(a, b)
    }

    @Test
    fun `hashCode is consistent with equality`() {
        val a = SyzygyID<User>("same-id")
        val b = SyzygyID<User>("same-id")
        assertEquals(a.hashCode(), b.hashCode())
    }
}

// ---------------------------------------------------------------------------
// Page<T>
// ---------------------------------------------------------------------------

class PageTests {
    @Test
    fun `isEmpty returns true when items is empty`() {
        val page = Page<String>(emptyList(), totalCount = 0, pageNumber = 1, pageSize = 20)
        assertTrue(page.isEmpty)
    }

    @Test
    fun `isEmpty returns false when items has content`() {
        val page = Page(listOf("a"), totalCount = 1, pageNumber = 1, pageSize = 20)
        assertFalse(page.isEmpty)
    }

    @Test
    fun `totalPages computed correctly for exact division`() {
        val page = Page<String>(emptyList(), totalCount = 40, pageNumber = 1, pageSize = 20)
        assertEquals(2, page.totalPages)
    }

    @Test
    fun `totalPages computed correctly for non-exact division`() {
        val page = Page<String>(emptyList(), totalCount = 41, pageNumber = 1, pageSize = 20)
        assertEquals(3, page.totalPages)
    }

    @Test
    fun `hasPreviousPage false for page 1`() {
        val page = Page<String>(emptyList(), totalCount = 100, pageNumber = 1, pageSize = 20)
        assertFalse(page.hasPreviousPage)
    }

    @Test
    fun `hasPreviousPage true for page 2`() {
        val page = Page<String>(emptyList(), totalCount = 100, pageNumber = 2, pageSize = 20)
        assertTrue(page.hasPreviousPage)
    }

    @Test
    fun `hasNextPage true when more items remain`() {
        val page = Page<String>(emptyList(), totalCount = 50, pageNumber = 1, pageSize = 20)
        assertTrue(page.hasNextPage)
    }

    @Test
    fun `hasNextPage false when on last page`() {
        val page = Page<String>(emptyList(), totalCount = 20, pageNumber = 1, pageSize = 20)
        assertFalse(page.hasNextPage)
    }
}

// ---------------------------------------------------------------------------
// PaginationRequest
// ---------------------------------------------------------------------------

class PaginationRequestTests {
    @Test
    fun `default values are pageNumber=1 pageSize=20 cursor=null`() {
        val req = PaginationRequest()
        assertEquals(1, req.pageNumber)
        assertEquals(20, req.pageSize)
        assertNull(req.cursor)
    }

    @Test
    fun `custom values set correctly`() {
        val req = PaginationRequest(pageNumber = 3, pageSize = 10, cursor = "next-cursor")
        assertEquals(3, req.pageNumber)
        assertEquals(10, req.pageSize)
        assertEquals("next-cursor", req.cursor)
    }
}

// ---------------------------------------------------------------------------
// SyzygyTimestamp
// ---------------------------------------------------------------------------

class SyzygyTimestampTests {
    @Test
    fun `now returns a positive millisecondsSinceEpoch value`() {
        val ts = SyzygyTimestamp.now()
        assertTrue(ts.millisecondsSinceEpoch > 0)
    }

    @Test
    fun `later timestamp is greater than earlier one`() {
        val earlier = SyzygyTimestamp(1_000L)
        val later = SyzygyTimestamp(2_000L)
        assertTrue(later > earlier)
    }

    @Test
    fun `secondsSinceEpoch is milliseconds divided by 1000`() {
        val ts = SyzygyTimestamp(5_000L)
        assertEquals(5.0, ts.secondsSinceEpoch)
    }
}

// ---------------------------------------------------------------------------
// SyzygyDuration
// ---------------------------------------------------------------------------

class SyzygyDurationTests {
    @Test
    fun `fromSeconds 1 equals 1000 milliseconds`() {
        assertEquals(1_000L, SyzygyDuration.seconds(1.0).milliseconds)
    }

    @Test
    fun `fromMinutes 1 equals 60000 milliseconds`() {
        assertEquals(60_000L, SyzygyDuration.minutes(1.0).milliseconds)
    }

    @Test
    fun `fromHours 1 equals 3600000 milliseconds`() {
        assertEquals(3_600_000L, SyzygyDuration.hours(1.0).milliseconds)
    }

    @Test
    fun `longer duration is greater than shorter`() {
        val short = SyzygyDuration.seconds(1.0)
        val long = SyzygyDuration.minutes(1.0)
        assertTrue(long > short)
    }
}

// ---------------------------------------------------------------------------
// ValidationResult
// ---------------------------------------------------------------------------

class ValidationResultTests {
    @Test
    fun `Valid case isValid is true`() {
        assertTrue(ValidationResult.Valid.isValid)
    }

    @Test
    fun `Valid case errorMessages is empty`() {
        assertTrue(ValidationResult.Valid.errorMessages.isEmpty())
    }

    @Test
    fun `Invalid case isValid is false`() {
        val result = ValidationResult.Invalid(listOf("too short"))
        assertFalse(result.isValid)
    }

    @Test
    fun `Invalid case messages contains expected strings`() {
        val result = ValidationResult.Invalid(listOf("too short", "missing digit"))
        assertEquals(listOf("too short", "missing digit"), result.messages)
    }
}

// ---------------------------------------------------------------------------
// NetworkRequest
// ---------------------------------------------------------------------------

class NetworkRequestTests {
    @Test
    fun `default timeout is 30 seconds`() {
        val req = NetworkRequest(url = "https://example.com", method = NetworkMethod.GET)
        assertEquals(30.0, req.timeoutSeconds)
    }

    @Test
    fun `GET method raw value is GET`() {
        assertEquals("GET", NetworkMethod.GET.value)
    }

    @Test
    fun `POST method raw value is POST`() {
        assertEquals("POST", NetworkMethod.POST.value)
    }

    @Test
    fun `DELETE method raw value is DELETE`() {
        assertEquals("DELETE", NetworkMethod.DELETE.value)
    }
}

// ---------------------------------------------------------------------------
// NetworkResponse
// ---------------------------------------------------------------------------

class NetworkResponseTests {
    private fun response(code: Int) = NetworkResponse(statusCode = code, data = ByteArray(0), headers = emptyMap())

    @Test
    fun `isSuccess true for 200`() {
        assertTrue(response(200).isSuccess)
    }

    @Test
    fun `isSuccess true for 201`() {
        assertTrue(response(201).isSuccess)
    }

    @Test
    fun `isSuccess true for 299`() {
        assertTrue(response(299).isSuccess)
    }

    @Test
    fun `isSuccess false for 300`() {
        assertFalse(response(300).isSuccess)
    }

    @Test
    fun `isSuccess false for 400`() {
        assertFalse(response(400).isSuccess)
    }

    @Test
    fun `isSuccess false for 500`() {
        assertFalse(response(500).isSuccess)
    }

    @Test
    fun `isClientError true for 400`() {
        assertTrue(response(400).isClientError)
    }

    @Test
    fun `isClientError true for 499`() {
        assertTrue(response(499).isClientError)
    }

    @Test
    fun `isClientError false for 500`() {
        assertFalse(response(500).isClientError)
    }

    @Test
    fun `isServerError true for 500`() {
        assertTrue(response(500).isServerError)
    }

    @Test
    fun `isServerError true for 599`() {
        assertTrue(response(599).isServerError)
    }

    @Test
    fun `isServerError false for 400`() {
        assertFalse(response(400).isServerError)
    }
}

// ---------------------------------------------------------------------------
// AuthToken
// ---------------------------------------------------------------------------

class AuthTokenTests {
    @Test
    fun `isExpired true when expiresAt is in the past`() {
        val past = SyzygyTimestamp(1L) // epoch + 1 ms, always in the past
        val token = AuthToken(accessToken = "tok", expiresAt = past)
        assertTrue(token.isExpired)
    }

    @Test
    fun `isExpired false when expiresAt is in the future`() {
        val future = SyzygyTimestamp(System.currentTimeMillis() + 3_600_000L)
        val token = AuthToken(accessToken = "tok", expiresAt = future)
        assertFalse(token.isExpired)
    }

    @Test
    fun `isExpired false when expiresAt is null`() {
        val token = AuthToken(accessToken = "tok", expiresAt = null)
        assertFalse(token.isExpired)
    }
}

// ---------------------------------------------------------------------------
// AuthState
// ---------------------------------------------------------------------------

class AuthStateTests {
    @Test
    fun `isAuthenticated true only for Authenticated`() {
        val token = AuthToken("tok")
        assertTrue(AuthState.Authenticated(token).isAuthenticated)
        assertFalse(AuthState.Unauthenticated.isAuthenticated)
        assertFalse(AuthState.Expired(token).isAuthenticated)
        assertFalse(AuthState.Refreshing.isAuthenticated)
    }

    @Test
    fun `token returns AuthToken for Authenticated`() {
        val token = AuthToken("tok")
        assertNotNull(AuthState.Authenticated(token).token)
    }

    @Test
    fun `token returns AuthToken for Expired`() {
        val token = AuthToken("tok")
        assertNotNull(AuthState.Expired(token).token)
    }

    @Test
    fun `token returns null for Unauthenticated`() {
        assertNull(AuthState.Unauthenticated.token)
    }

    @Test
    fun `token returns null for Refreshing`() {
        assertNull(AuthState.Refreshing.token)
    }
}

// ---------------------------------------------------------------------------
// SyzygyEnvironment
// ---------------------------------------------------------------------------

class SyzygyEnvironmentTests {
    @Test
    fun `isDebug true only for DEBUG`() {
        assertTrue(SyzygyEnvironment.DEBUG.isDebug)
        assertFalse(SyzygyEnvironment.STAGING.isDebug)
        assertFalse(SyzygyEnvironment.PRODUCTION.isDebug)
    }

    @Test
    fun `isProduction true only for PRODUCTION`() {
        assertTrue(SyzygyEnvironment.PRODUCTION.isProduction)
        assertFalse(SyzygyEnvironment.DEBUG.isProduction)
        assertFalse(SyzygyEnvironment.STAGING.isProduction)
    }

    @Test
    fun `description string correct for each case`() {
        assertEquals("debug", SyzygyEnvironment.DEBUG.toString())
        assertEquals("staging", SyzygyEnvironment.STAGING.toString())
        assertEquals("production", SyzygyEnvironment.PRODUCTION.toString())
    }
}

// ---------------------------------------------------------------------------
// SyzygyVersion
// ---------------------------------------------------------------------------

class SyzygyVersionTests {
    @Test
    fun `1 5 0 is greater than 1 4 9`() {
        assertTrue(SyzygyVersion(1, 5, 0) > SyzygyVersion(1, 4, 9))
    }

    @Test
    fun `2 0 0 is greater than 1 9 9`() {
        assertTrue(SyzygyVersion(2, 0, 0) > SyzygyVersion(1, 9, 9))
    }

    @Test
    fun `1 0 0 equals 1 0 0`() {
        assertEquals(SyzygyVersion(1, 0, 0), SyzygyVersion(1, 0, 0))
    }

    @Test
    fun `toString produces major dot minor dot patch`() {
        assertEquals("1.2.3", SyzygyVersion(1, 2, 3).toString())
    }

    @Test
    fun `toString produces major dot minor dot patch dash prerelease when prerelease set`() {
        assertEquals("1.2.3-beta", SyzygyVersion(1, 2, 3, prerelease = "beta").toString())
    }

    @Test
    fun `current version matches release`() {
        assertEquals("2.0.0", SyzygyVersion.current.toString())
    }
}

// ---------------------------------------------------------------------------
// SyzygyErrorCode
// ---------------------------------------------------------------------------

class SyzygyErrorCodeTests {
    @Test
    fun `static constants exist`() {
        assertNotNull(SyzygyErrorCode.unknown)
        assertNotNull(SyzygyErrorCode.timeout)
        assertNotNull(SyzygyErrorCode.notFound)
        assertNotNull(SyzygyErrorCode.cancelled)
        assertNotNull(SyzygyErrorCode.unauthenticated)
        assertNotNull(SyzygyErrorCode.serverError)
        assertNotNull(SyzygyErrorCode.networkUnavailable)
        assertNotNull(SyzygyErrorCode.decodingFailed)
        assertNotNull(SyzygyErrorCode.encodingFailed)
    }

    @Test
    fun `equality works — same rawValue are equal`() {
        val a = SyzygyErrorCode("timeout")
        val b = SyzygyErrorCode("timeout")
        assertEquals(a, b)
    }

    @Test
    fun `equality works — different rawValues are not equal`() {
        assertNotEquals(SyzygyErrorCode("timeout"), SyzygyErrorCode("not_found"))
    }
}

// ---------------------------------------------------------------------------
// MockLogger (testingSupport)
// ---------------------------------------------------------------------------

class MockLoggerTests {
    private fun entry(
        level: LogLevel,
        message: String,
    ) = LogEntry(level = level, message = message, timestamp = SyzygyTimestamp.now())

    @Test
    fun `log stores entries`() {
        val logger = MockLogger()
        logger.log(entry(LogLevel.INFO, "hello"))
        assertEquals(1, logger.entries.size)
    }

    @Test
    fun `entries forLevel filters by level correctly`() {
        val logger = MockLogger()
        logger.log(entry(LogLevel.INFO, "info msg"))
        logger.log(entry(LogLevel.ERROR, "error msg"))
        logger.log(entry(LogLevel.INFO, "info msg 2"))
        val infoEntries = logger.entries(forLevel = LogLevel.INFO)
        assertEquals(2, infoEntries.size)
        assertTrue(infoEntries.all { it.level == LogLevel.INFO })
    }

    @Test
    fun `clear empties entries`() {
        val logger = MockLogger()
        logger.log(entry(LogLevel.DEBUG, "debug"))
        logger.clear()
        assertTrue(logger.entries.isEmpty())
    }
}

// ---------------------------------------------------------------------------
// MockStorageProvider (testingSupport)
// ---------------------------------------------------------------------------

class MockStorageProviderTests {
    private val stringKey = StorageKey<String>("test-key")

    @Test
    fun `set then get returns correct value`() {
        val store = MockStorageProvider()
        store.set("hello", stringKey) { it }
        val result = store.get(stringKey) { it }
        assertEquals("hello", result)
    }

    @Test
    fun `remove deletes value`() {
        val store = MockStorageProvider()
        store.set("hello", stringKey) { it }
        store.remove(stringKey)
        assertNull(store.get(stringKey) { it })
    }

    @Test
    fun `clear empties all storage`() {
        val store = MockStorageProvider()
        store.set("a", StorageKey("key1")) { it }
        store.set("b", StorageKey("key2")) { it }
        store.clear()
        assertTrue(store.storage.isEmpty())
    }
}

// ---------------------------------------------------------------------------
// SpyAnalyticsProvider (testingSupport)
// ---------------------------------------------------------------------------

class SpyAnalyticsProviderTests {
    @Test
    fun `track records event`() {
        val spy = SpyAnalyticsProvider()
        spy.track(AnalyticsEvent(name = "button_tapped"))
        assertEquals(1, spy.trackedEvents.size)
    }

    @Test
    fun `events named filters correctly`() {
        val spy = SpyAnalyticsProvider()
        spy.track(AnalyticsEvent(name = "page_view"))
        spy.track(AnalyticsEvent(name = "button_tapped"))
        spy.track(AnalyticsEvent(name = "page_view"))
        assertEquals(2, spy.events(named = "page_view").size)
    }

    @Test
    fun `identify records user`() {
        val spy = SpyAnalyticsProvider()
        spy.identify("user-123", mapOf("plan" to "pro"))
        assertEquals(1, spy.identifiedUsers.size)
        assertEquals("user-123", spy.identifiedUsers.first().first)
    }

    @Test
    fun `reset increments resetCallCount`() {
        val spy = SpyAnalyticsProvider()
        spy.reset()
        spy.reset()
        assertEquals(2, spy.resetCallCount)
    }
}

// ---------------------------------------------------------------------------
// FixedTimeProvider (testingSupport)
// ---------------------------------------------------------------------------

class FixedTimeProviderTests {
    @Test
    fun `now returns the fixed time set`() {
        val fixed = SyzygyTimestamp(12_345L)
        val provider = FixedTimeProvider(fixed)
        assertEquals(fixed, provider.now())
    }

    @Test
    fun `since computes duration from fixedTime correctly`() {
        val provider = FixedTimeProvider(SyzygyTimestamp(5_000L))
        val earlier = SyzygyTimestamp(3_000L)
        val duration = provider.since(earlier)
        assertEquals(2_000L, duration.milliseconds)
    }
}

// ---------------------------------------------------------------------------
// MockNetworkClient (testingSupport)
// ---------------------------------------------------------------------------

class MockNetworkClientTests {
    private val request =
        NetworkRequest(
            url = "https://example.com",
            method = NetworkMethod.GET,
            headers = emptyMap(),
        )

    @Test
    fun `execute returns queued response`() =
        runBlocking {
            val client = MockNetworkClient()
            val response = NetworkResponse(statusCode = 200, data = ByteArray(0), headers = emptyMap())
            client.responses.add(response)
            val result = client.execute(request)
            assertEquals(200, result.statusCode)
        }

    @Test
    fun `execute records request`() =
        runBlocking {
            val client = MockNetworkClient()
            client.responses.add(NetworkResponse(statusCode = 200, data = ByteArray(0), headers = emptyMap()))
            client.execute(request)
            assertEquals(1, client.requests.size)
            assertEquals("https://example.com", client.requests.first().url)
        }

    @Test
    fun `execute throws configured error`() =
        runBlocking {
            val client = MockNetworkClient()
            client.error = RuntimeException("network error")
            var thrown: Throwable? = null
            try {
                client.execute(request)
            } catch (e: Throwable) {
                thrown = e
            }
            assertNotNull(thrown)
            assertEquals("network error", thrown?.message)
        }
}

// ---------------------------------------------------------------------------
// MockConnectivityProvider (testingSupport)
// ---------------------------------------------------------------------------

class MockConnectivityProviderTests {
    @Test
    fun `default initial state is CONNECTED`() {
        val provider = MockConnectivityProvider()
        assertEquals(ConnectivityState.CONNECTED, provider.state.value)
        assertTrue(provider.isConnected)
    }

    @Test
    fun `setState to DISCONNECTED updates isConnected`() {
        val provider = MockConnectivityProvider()
        provider.setState(ConnectivityState.DISCONNECTED)
        assertFalse(provider.isConnected)
        assertEquals(ConnectivityState.DISCONNECTED, provider.state.value)
    }

    @Test
    fun `constructor initial state overrides default`() {
        val provider = MockConnectivityProvider(ConnectivityState.DISCONNECTED)
        assertFalse(provider.isConnected)
    }
}

// ---------------------------------------------------------------------------
// MockAuthProvider (testingSupport)
// ---------------------------------------------------------------------------

class MockAuthProviderTests {
    @Test
    fun `initial state is Unauthenticated`() {
        val provider = MockAuthProvider()
        assertFalse(provider.state.value.isAuthenticated)
    }

    @Test
    fun `authenticate transitions to Authenticated`() {
        val provider = MockAuthProvider()
        val token = AuthToken(accessToken = "tok")
        provider.authenticate(token)
        assertTrue(provider.state.value.isAuthenticated)
    }

    @Test
    fun `signOut transitions to Unauthenticated and increments count`() {
        val provider = MockAuthProvider()
        provider.authenticate(AuthToken(accessToken = "tok"))
        provider.signOut()
        assertFalse(provider.state.value.isAuthenticated)
        assertEquals(1, provider.signOutCallCount)
    }

    @Test
    fun `refresh increments refreshCallCount`() =
        runBlocking {
            val provider = MockAuthProvider()
            provider.refresh()
            provider.refresh()
            assertEquals(2, provider.refreshCallCount)
        }
}
