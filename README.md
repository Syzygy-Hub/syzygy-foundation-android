[![Android](https://img.shields.io/badge/Android-Kotlin-7F77DD?style=flat)](https://developer.android.com/) [![Kotlin](https://img.shields.io/badge/Kotlin-2.0-1D9E75?logo=kotlin&logoColor=white&style=flat)](https://kotlinlang.org) [![CI](https://img.shields.io/github/actions/workflow/status/Syzygy-Hub/syzygy-foundation-android/ci.yml?label=ci&style=flat)](https://github.com/Syzygy-Hub/syzygy-foundation-android/actions/workflows/ci.yml) [![Version](https://img.shields.io/badge/version-1.2.0-D85A30?style=flat)](https://github.com/Syzygy-Hub/syzygy-foundation-android/releases) [![License](https://img.shields.io/badge/License-MIT-green?style=flat)](LICENSE)

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="https://raw.githubusercontent.com/Syzygy-Hub/.github/main/brand/assets/banners/syzygy-banner-dark-1200.png">
  <img src="https://raw.githubusercontent.com/Syzygy-Hub/.github/main/brand/assets/banners/syzygy-banner-light-1200.png" alt="Syzygy" width="600">
</picture>

# syzygy-foundation-android

The root layer of the Syzygy ecosystem — providing SharedTypes, base protocols, and shared contracts that every peer layer builds on.

## About

syzygy-foundation-android is the base layer every other Syzygy Android library depends on. It defines the interfaces that Services implements, the value types that UI and Core consume, and the error types the whole stack shares. Nothing in Foundation has behaviour beyond property storage — no network calls, no platform APIs, no business logic. Swap any implementation in Services or Core by conforming to these contracts; Foundation never needs to change.

## Role in the Syzygy Ecosystem

`syzygy-foundation-android` is the root layer — the only dependency shared by all peer layers. It depends on nothing. Every peer layer (UI, Core, Services, AI) depends on Foundation and nothing else.

Full ecosystem architecture: [ecosystem-fragment.md](https://github.com/Syzygy-Hub/.github/blob/main/docs/ecosystem-fragment.md)

### Shared Contracts

Foundation defines the shared contracts that all peer layers consume. These contracts are the abstraction layer that allows UI, Core, Services and AI to each depend on Foundation without depending on each other.

- **`NetworkClientProtocol`** — abstracts HTTP networking so any peer layer can make network requests without depending on a concrete implementation. `syzygy-services-android` provides the concrete OkHttp implementation.
- **`AuthProvider`** — abstracts authentication and token management. `syzygy-services-android` provides the concrete OAuth and SharedPreferences implementations.
- **`StorageProvider`** — abstracts local persistence. `syzygy-services-android` provides the concrete SharedPreferences implementation.
- **`LoggerProtocol`** — abstracts logging and observability so all peer layers can log without depending on a specific logging framework.

> These contracts are currently defined as planned interfaces. Concrete implementations will ship with `syzygy-services-android` in Phase 2 of the ecosystem roadmap.

## Release Process

Releases follow the Syzygy tag-push release flow:

1. Create a `release/X.X.X` branch
2. Bump the version in `syzygy.yml`, `build.gradle.kts` (`syzygyVersion` variable), the README badge, and `CHANGELOG.md`
3. Open a PR to `main` and wait for CI to pass
4. Merge the PR
5. Push the tag: `git tag X.X.X` and `git push origin X.X.X`
6. The tag push triggers the org-level release workflow which validates `syzygy.yml` matches the tag, extracts the CHANGELOG entry, and creates the GitHub Release. JitPack auto-builds from the tag.

For the full release standard see the [Syzygy-Hub/.github release standard](https://github.com/Syzygy-Hub/.github/blob/main/engineering/standards/release-standard.md).

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
implementation("com.github.Syzygy-Hub:syzygy-foundation-android:1.2.0")
testImplementation("com.github.Syzygy-Hub:syzygy-foundation-android:1.2.0") // for testingSupport
```

## Architecture

SyzygyFoundation exposes two source sets:

- **main** — runtime contracts and primitives. Add as `implementation`.
- **testingSupport** — test support. Add as `testImplementation` only.

**Depends on:** nothing

**Used by:** syzygy-ui-android, syzygy-core-android, syzygy-services-android, syzygy-ai-android

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

## License

MIT — see [LICENSE](LICENSE)
