[![Android](https://img.shields.io/badge/Android-Kotlin-7F77DD?style=flat)](https://developer.android.com/) [![Kotlin](https://img.shields.io/badge/Kotlin-2.0-1D9E75?logo=kotlin&logoColor=white&style=flat)](https://kotlinlang.org) [![CI](https://img.shields.io/github/actions/workflow/status/Syzygy-Hub/syzygy-foundation-android/ci.yml?label=ci&style=flat)](https://github.com/Syzygy-Hub/syzygy-foundation-android/actions/workflows/ci.yml) [![Version](https://img.shields.io/badge/version-1.0.0-D85A30?style=flat)](https://github.com/Syzygy-Hub/syzygy-foundation-android/releases) [![License](https://img.shields.io/badge/License-MIT-green?style=flat)](LICENSE)

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="https://raw.githubusercontent.com/Syzygy-Hub/.github/main/brand/syzygy-banner-dark-1200.png">
  <img src="https://raw.githubusercontent.com/Syzygy-Hub/.github/main/brand/syzygy-banner-light-1200.png" alt="Syzygy" width="600">
</picture>

# syzygy-foundation-android

Contracts, primitives, and shared types for the Android Syzygy ecosystem — zero implementation, zero dependencies.

## About

syzygy-foundation-android is the base layer every other Syzygy Android library depends on. It defines the interfaces that Services implements, the value types that UI and Core consume, and the error types the whole stack shares. Nothing in Foundation has behaviour beyond property storage — no network calls, no platform APIs, no business logic. Swap any implementation in Services or Core by conforming to these contracts; Foundation never needs to change.

## Platforms

| Platform | Min Version | Package Manager | Status |
|---|---|---|---|
| Android | API 26+ | JitPack | ✅ Supported |

## Requirements

- Android API 26+
- Kotlin 2.0+
- Android Studio Ladybug or later
- AGP 8.0+

## Installation

```kotlin
// In settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
}

// In build.gradle.kts
implementation("com.github.Syzygy-Hub:syzygy-foundation-android:1.0.0")
testImplementation("com.github.Syzygy-Hub:syzygy-foundation-android:1.0.0") // for testingSupport
```

## Architecture

SyzygyFoundation exposes two source sets:

- **main** — runtime contracts and primitives. Add as `implementation`.
- **testingSupport** — test support. Add as `testImplementation` only.

**Depends on:** nothing

**Used by:** syzygy-ui-android, syzygy-core-android, syzygy-services-android

For the full ecosystem architecture see [syzygy-ecosystem.md](https://github.com/Syzygy-Hub/.github/blob/main/engineering/architecture/syzygy-ecosystem.md).

## API

### Primitives

- `SyzygyID<T>` — phantom-typed UUID-backed identifier preventing accidental ID mixing
- `Page<T>` / `PaginationRequest` — paginated data structures
- `SyzygyTimestamp` / `SyzygyDuration` / `TimeProvider` — cross-platform time primitives
- `ValidationResult` / `ValidationRule` — validation contract and result type

### Contracts

- `NetworkClientProtocol` / `NetworkRequest` / `NetworkResponse` — networking contract
- `StorageProvider` / `StorageKey` — type-safe storage contract
- `AuthProvider` / `AuthToken` / `AuthState` — authentication contract
- `AnalyticsProvider` / `AnalyticsEvent` — analytics contract
- `LoggerProtocol` / `LogLevel` / `LogEntry` — logging contract
- `ConnectivityProvider` / `ConnectivityState` — connectivity contract

### Shared Types

- `SyzygyEnvironment` — debug / staging / production
- `SyzygyConfiguration` — app configuration contract
- `SyzygyBuildInfo` — consumer-injected build metadata
- `SyzygyVersion` — semantic version with comparison support

### Errors

- `SyzygyError` — base error interface
- `SyzygyErrorCode` — typed, extensible error codes
- `SyzygyErrorSeverity` — error severity levels

### Testing Support

Add `testingSupport` output as `testImplementation` only.

- `MockLogger`, `MockConnectivityProvider`, `MockAuthProvider`, `MockStorageProvider`, `MockNetworkClient`
- `SpyAnalyticsProvider`
- `FixtureProvider`, `FixedTimeProvider`

## Usage

### Implementing a contract

```kotlin
import com.syzygyhub.foundation.*

class OkHttpNetworkClient : NetworkClientProtocol {
    private val client = OkHttpClient()

    override suspend fun execute(request: NetworkRequest): NetworkResponse {
        val req = Request.Builder().url(request.url)
            .method(request.method.value, request.body?.toRequestBody())
            .build()
        val resp = client.newCall(req).await()
        return NetworkResponse(
            statusCode = resp.code,
            data = resp.body?.bytes() ?: byteArrayOf(),
            headers = emptyMap()
        )
    }
}
```

### Using a primitive

```kotlin
import com.syzygyhub.foundation.*

data class User(val id: SyzygyID<User>)
data class Post(val id: SyzygyID<Post>)

val userId = SyzygyID.generate<User>()
val postId = SyzygyID.generate<Post>()
// userId == postId  // always false — distinct phantom types
```

### Using test support

```kotlin
import com.syzygyhub.foundation.testing.*

@Test
fun `fetch returns data`() = runTest {
    val client = MockNetworkClient()
    client.enqueue(NetworkResponse(statusCode = 200, data = byteArrayOf(), headers = emptyMap()))
    val result = client.execute(NetworkRequest(url = "https://example.com", method = NetworkMethod.GET))
    assertEquals(200, result.statusCode)
}
```

## Platform Notes

- Async pattern: `suspend` functions / Kotlin coroutines
- `SyzygyError` is a Kotlin interface — implementations must extend `Exception`
- `ConnectivityProvider`: self-detecting via `ConnectivityManager`
- `SyzygyBuildInfo`: inject at app startup from `BuildConfig`

## Contributing

Contributions are welcome. Please follow the [Syzygy engineering standards](https://github.com/Syzygy-Hub/.github/tree/main/engineering/standards) when submitting pull requests.

## Releases

Releases follow the Syzygy commit-message flow:

1. Create branch `release/X.X.X`
2. Bump version in manifest and `syzygy.yml`
3. Update `CHANGELOG.md`
4. Open PR → `main`
5. Get approval and merge with commit message starting with **`release:`** (e.g. `release: 1.0.0`)
6. CI detects the `release:` prefix → reads version from `syzygy.yml` → creates git tag and GitHub Release automatically

See the [Syzygy Release Standard](https://github.com/Syzygy-Hub/.github/blob/main/engineering/standards/release-standard.md) for full details.

## License

MIT — see [LICENSE](LICENSE)
