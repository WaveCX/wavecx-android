# WaveCX Android SDK

The WaveCx Android SDK provides tools to integrate targeted content and user-triggered modals into your Android applications.

**Version:** 1.1.0

## Installation

### Option 1: Using the AAR directly

Download `lib/wavecx-android-sdk.aar` and add it to your project:

1. Copy `wavecx-android-sdk.aar` to your app's `libs/` directory (or any location)
2. Add to your app's `build.gradle.kts`:

```kotlin
dependencies {
    // If you copied to libs/ directory:
    implementation(files("libs/wavecx-android-sdk.aar"))

    // Or use absolute/relative path:
    // implementation(files("path/to/wavecx-android-sdk.aar"))

    // Required dependencies
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.google.android.material:material:1.12.0")
}
```

### Option 2: Using JitPack (recommended for production)

Add to your `settings.gradle.kts`:
```kotlin
dependencyResolutionManagement {
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
}
```

Add to your app's `build.gradle.kts`:
```kotlin
dependencies {
    implementation("com.github.wavecx:wavecx-android:1.1.0")
}
```

## Quick Start

```kotlin
// 1. Initialize the SDK
WaveCx.initializeDefaultInstance(
    context = this,
    organizationCode = "your-org-code"
)

// 2. Set up a listener
val waveCx = WaveCx.defaultInstance()
waveCx.setListener(object : WaveCxListener {
    override fun onContentReceived(content: List<Content>) {
        // Content loaded
    }
})

// 3. Start a user session
waveCx.startUserSession(userId = "user123")

// 4. Trigger content at key points
waveCx.triggerPoint("home-screen")
```

## Example App

See `examples/kotlin-compose/` for a complete working example built with Jetpack Compose. The example demonstrates:

- SDK initialization
- User session management
- Trigger points
- User-triggered content
- Mock mode for testing
- Event tracking

To run the example:
```bash
cd examples/kotlin-compose
./gradlew installDebug
```

## Documentation

- [Full Documentation](README.md) - Complete SDK documentation
- [Changelog](CHANGELOG.md) - Version history and changes
- [Release Notes](RELEASE_NOTES_1.1.0.md) - Details about this release

## Features

- Start a user session with attributes
- Trigger content based on specific trigger points
- Display user-triggered content modals
- Debug mode for detailed logging
- Mock mode for testing without API calls
- Thread-safe operations throughout
- Automatic retry with exponential backoff
- ProGuard/R8 compatible

## Requirements

- Android 5.0 (API 21) or higher
- Kotlin 1.9.0 or higher

## Support

For issues and questions, please contact WaveCx support or open an issue in the repository.

## License

Copyright © 2025 WaveCx. All rights reserved.
