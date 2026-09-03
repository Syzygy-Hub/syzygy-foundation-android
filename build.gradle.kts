plugins {
    id("org.jetbrains.kotlin.jvm") version "2.0.21"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.2"
    id("maven-publish")
}

// Single canonical version source — bump only this value on each release.
val syzygyVersion = "1.1.0"

group = "com.github.Syzygy-Hub"
version = syzygyVersion

kotlin {
    jvmToolchain(17)
}

// ---------------------------------------------------------------------------
// Source sets
// ---------------------------------------------------------------------------

sourceSets {
    main {
        kotlin.srcDirs("src/main/kotlin")
    }
    // testingSupport: a dedicated source set whose classes are NOT included in
    // the release artifact. Consumers add the testingSupport JAR only in their
    // test/debug configurations.
    create("testingSupport") {
        kotlin.srcDirs("src/testingSupport/kotlin")
        compileClasspath += sourceSets["main"].output
        runtimeClasspath += sourceSets["main"].output
    }
}

dependencies {
    // kotlinx-coroutines-core is JetBrains first-party (part of the Kotlin
    // toolchain) and is required for StateFlow used by AuthProvider and
    // ConnectivityProvider.
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")

    // testingSupport compileOnly depends on main — no extra runtime deps.
    "testingSupportImplementation"("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")

    // Unit tests — JUnit 5 (Jupiter) via the Kotlin test wrapper.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:2.0.21")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Make testingSupport classes available inside the test source set so that
    // tests can use MockLogger, SpyAnalyticsProvider, FixedTimeProvider, etc.
    testImplementation(sourceSets["testingSupport"].output)
}

// ---------------------------------------------------------------------------
// Publishing — JitPack
// ---------------------------------------------------------------------------

// Artifact for the main runtime source set (released to JitPack).
val mainSourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

// Artifact for the testingSupport source set (separate classifier; consumed
// only in test/debug configurations by downstream modules).
val testingSupportJar by tasks.registering(Jar::class) {
    archiveClassifier.set("testing-support")
    from(sourceSets["testingSupport"].output)
    dependsOn(tasks.named("compileTestingSupportKotlin"))
}

val testingSupportSourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("testing-support-sources")
    from(sourceSets["testingSupport"].allSource)
}

publishing {
    publications {
        create<MavenPublication>("release") {
            from(components["java"])
            groupId = "com.github.Syzygy-Hub"
            artifactId = "syzygy-foundation-android"
            version = syzygyVersion
            artifact(mainSourcesJar)
        }
        create<MavenPublication>("testingSupport") {
            groupId = "com.github.Syzygy-Hub"
            artifactId = "syzygy-foundation-android-testing"
            version = syzygyVersion
            artifact(testingSupportJar)
            artifact(testingSupportSourcesJar)
        }
    }
}

// Use JUnit Platform (JUnit 5) as the test engine.
tasks.withType<Test> {
    useJUnitPlatform()
}

// ---------------------------------------------------------------------------
// ktlint — lint main and testingSupport Kotlin sources directly via ktlint-cli
// (the ktlint-gradle plugin's source-set auto-discovery is unreliable in
// non-standard source set configurations; driving ktlint-cli directly is the
// same approach used in syzygy-ui-android).
// ---------------------------------------------------------------------------

val ktlintCli: Configuration by configurations.creating

dependencies {
    ktlintCli("com.pinterest.ktlint:ktlint-cli:1.0.1")
}

val ktlintCheckSources by tasks.registering(JavaExec::class) {
    group = "verification"
    description = "Runs ktlint against src/main/**/*.kt and src/testingSupport/**/*.kt"
    classpath = ktlintCli
    mainClass.set("com.pinterest.ktlint.Main")
    args = listOf("src/main/**/*.kt", "src/testingSupport/**/*.kt")
    workingDir = project.projectDir
}

tasks.named("ktlintCheck") {
    dependsOn(ktlintCheckSources)
}

val ktlintFormatSources by tasks.registering(JavaExec::class) {
    group = "formatting"
    description = "Auto-fixes ktlint violations in src/**/*.kt"
    classpath = ktlintCli
    mainClass.set("com.pinterest.ktlint.Main")
    args = listOf("-F", "src/main/**/*.kt", "src/testingSupport/**/*.kt")
    workingDir = project.projectDir
}

tasks.named("ktlintFormat") {
    dependsOn(ktlintFormatSources)
}
