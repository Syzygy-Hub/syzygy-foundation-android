# Changelog

All notable changes to `syzygy-foundation-android` are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/) and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [Unreleased]

### Added

### Changed

### Fixed

---

## [1.1.0] — 2026-09-03

### Changed
- Ecosystem repositioned as AI-enabled cross-platform engineering framework for mobile, web and enterprise
- CI workflow refactored — inline release job removed, release now handled by org-level tag-push workflow
- Lint configuration migrated to `Syzygy-Hub/.github/engineering/tooling/`
- Lint step reordered to run before build and test
- `build.gradle.kts` refactored to extract version to a single `syzygyVersion` variable (was hardcoded in 3 places)
- README updated with ecosystem architecture, shared contracts documentation, and release process

### Added
- `syzygy.yml` confirmed as canonical version source of truth
- Shared contracts section documenting `NetworkClientProtocol`, `AuthProvider`, `StorageProvider`, `LoggerProtocol`

---

## [1.0.0] - 2026-08-06

### Added

#### Primitives
- `SyzygyID<T>` — phantom-typed, UUID-backed identifier; `Comparable`; `generate()` factory
- `Page<T>` — paginated result container with `hasNextPage`, `hasPreviousPage`, `isEmpty`, `totalPages`
- `PaginationRequest` — page number, page size, optional cursor; defaults to page 1, size 20
- `SyzygyTimestamp` — millisecond-precision Unix-epoch instant; `Comparable`; `toInstant()`; `now()` factory
- `SyzygyDuration` — millisecond-precision duration; `.seconds(_:)`, `.minutes(_:)`, `.hours(_:)` companion factories; `Comparable`
- `TimeProvider` — interface for injectable clock abstraction
- `ValidationResult` — `Valid` / `Invalid(messages)` sealed class; `isValid`, `errorMessages` helpers
- `ValidationRule<T>` — single-method validation contract

#### Contracts — Network
- `NetworkMethod` — `GET`, `POST`, `PUT`, `PATCH`, `DELETE`, `HEAD` enum with `value` string
- `NetworkRequest` — URL + method + headers + `ByteArray` body + timeout; custom `equals`/`hashCode` handle `ByteArray`
- `NetworkResponse` — status code + `ByteArray` data + headers; `isSuccess`, `isClientError`, `isServerError`; custom `equals`/`hashCode`
- `NetworkClientProtocol` — `suspend fun execute(request): NetworkResponse` contract

#### Contracts — Storage
- `StorageKey<T>` — phantom-typed string key with optional `defaultValue`
- `StorageProvider` — `get`/`set`/`remove`/`clear` contract using explicit serializer/deserializer lambdas

#### Contracts — Auth
- `AuthToken` — access token, optional refresh token, optional expiry; `isExpired` computed property
- `AuthState` — `Unauthenticated`, `Authenticated(authToken)`, `Expired(authToken)`, `Refreshing` sealed class; `isAuthenticated`, `token` helpers
- `AuthProvider` — `StateFlow<AuthState>` + `authenticate`, `refresh`, `signOut` contract

#### Contracts — Analytics
- `AnalyticsEvent` — name + `Map<String, String>` properties + `SyzygyTimestamp` timestamp
- `AnalyticsProvider` — `track`, `identify(userId, traits)`, `reset` contract

#### Contracts — Logging
- `LogLevel` — `DEBUG(0)`, `INFO(1)`, `WARNING(2)`, `ERROR(3)`, `CRITICAL(4)` ordered enum; `Comparable`
- `LogEntry` — level + message + timestamp + `Map<String, String>` metadata + optional `Throwable`; `error` excluded from `equals`/`hashCode`
- `LoggerProtocol` — abstract `log(entry)` + `debug`, `info`, `warning`, `error`, `critical` default methods

#### Contracts — Connectivity
- `ConnectivityState` — `CONNECTED`, `DISCONNECTED`, `UNKNOWN` enum with `isConnected`
- `ConnectivityProvider` — `StateFlow<ConnectivityState>` + synchronous `isConnected` snapshot

#### Shared Types
- `SyzygyEnvironment` — `DEBUG`, `STAGING`, `PRODUCTION` enum; `isDebug`, `isProduction`; lowercase `toString()`
- `SyzygyConfiguration` — interface for top-level application configuration
- `SyzygyBuildInfo` — consumer-injected app metadata (name, bundle ID, build number, version string)
- `SyzygyVersion` — semantic version `major.minor.patch[-prerelease]`; `Comparable`; `SyzygyVersion.current` = `1.0.0`

#### Errors
- `SyzygyErrorSeverity` — `INFO(0)`, `WARNING(1)`, `ERROR(2)`, `CRITICAL(3)` ordered enum
- `SyzygyErrorCode` — `@JvmInline` value class wrapping String; predefined: `unknown`, `cancelled`, `timeout`, `unauthenticated`, `forbidden`, `notFound`, `serverError`, `networkUnavailable`, `decodingFailed`, `encodingFailed`
- `SyzygyError` — `Throwable`-extending interface; `code`, `message`, `severity`, `underlyingError`; implementations extend `Exception` by convention

#### Testing Support (`testingSupport` source set — `testImplementation` only)
- `MockLogger` — records `LogEntry` instances; `entries(forLevel:)` filter; `clear()`
- `MockConnectivityProvider` — `MutableStateFlow`-backed; `setState()` for test control
- `MockAuthProvider` — tracks `refreshCallCount`, `signOutCallCount`; configurable `refreshResult`
- `MockStorageProvider` — in-memory string map; `storage` accessible for assertions
- `MockNetworkClient` — FIFO response queue; `requests` list; configurable `error`
- `SpyAnalyticsProvider` — records `trackedEvents`, `identifiedUsers`, `resetCallCount`; `events(named:)` filter
- `FixtureProvider<T>` — marker interface for fixture factories
- `FixedTimeProvider` — deterministic `TimeProvider` with mutable `fixedTime`

#### Repository
- `syzygy.yml` manifest added

### Fixed
- Comprehensive unit test suite added: 72 tests across 17 test classes covering all Foundation types; 0 failures
- Gradle daemon JVM downgraded from 25 to 21 in `gradle/gradle-daemon-jvm.properties` — Kotlin 2.0.21 has a version-parsing bug (`IllegalArgumentException`) with JDK 25; JDK 21 (LTS) is fully compatible
- JUnit 5 (Jupiter) dependencies added to `build.gradle.kts`; `testingSupport` output added to `test` classpath

### Changed
- CI lint config source updated: `ktlint` step now fetches `.editorconfig` from `Syzygy-Hub/.github/main/engineering/tooling/android/`
- CI Java version updated from 17 to 21 to match `gradle-daemon-jvm.properties`
- CI coverage summary step added (test report path written to `GITHUB_STEP_SUMMARY`)
- README rewritten to Syzygy engineering standard

[Unreleased]: https://github.com/Syzygy-Hub/syzygy-foundation-android/compare/1.1.0...HEAD
[1.1.0]: https://github.com/Syzygy-Hub/syzygy-foundation-android/compare/1.0.0...1.1.0
[1.0.0]: https://github.com/Syzygy-Hub/syzygy-foundation-android/releases/tag/1.0.0
