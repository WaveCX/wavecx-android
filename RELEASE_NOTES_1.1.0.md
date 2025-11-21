# WaveCx Android SDK v1.1.0

**Release Date:** January 20, 2025

Version 1.1.0 focuses on production readiness with comprehensive improvements to thread safety, memory management, and developer experience.

## What's New

### Debug Mode

Enable detailed logging to understand SDK behavior during development:

```kotlin
WaveCx.initializeDefaultInstance(
    context = this,
    organizationCode = "your-org-code",
    debugMode = true  // Enables detailed logging
)
```

Debug mode logs API calls, content loading, trigger points, and lifecycle events - perfect for troubleshooting integration issues.

### Mock Mode for Testing

Test your integration without making API calls using mock mode. Use the built-in `generateMockContent()` helper to quickly create test content:

```kotlin
val mockContent = generateMockContent(listOf("home-screen", "checkout", "profile"))
val mockConfig = MockModeConfig(
    enabled = true,
    customContent = mockContent
)

WaveCx.initializeDefaultInstance(
    context = this,
    organizationCode = "your-org-code",
    mockModeConfig = mockConfig
)
```

Mock mode lets you develop and test without backend dependencies. The example app demonstrates a complete mock mode implementation.

### Complete Example App

A full-featured example app is now included, demonstrating:
- SDK initialization with debug and mock modes
- User session management with attributes
- Trigger points throughout the app flow
- User-triggered content (buttons)
- Event tracking
- Modern Jetpack Compose UI

Check it out in `examples/kotlin-compose/`.

### Automatic Retry for Content Loading

Content loading now handles transient network issues automatically. If content fails to load, the SDK will retry up to 3 times with increasing delays (500ms, 1000ms, 2000ms). After automatic retries are exhausted, users see an error screen with a manual retry button.

### Runtime Version Access

You can now check the SDK version at runtime:

```kotlin
val version = WaveCx.VERSION      // "1.1.0"
val versionCode = WaveCx.VERSION_CODE  // 2
```

### ProGuard/R8 Support

The SDK now includes consumer ProGuard rules that preserve all public APIs and enable code minification in release builds. No additional configuration needed.

### Enhanced Thread Safety

All SDK operations are now thread-safe. You can call any public method from any thread, and all listener callbacks are guaranteed to execute on the main thread.

## Improvements

### Memory Management

Several improvements prevent memory leaks:
- WebView cleanup when activities are destroyed
- Handler callbacks cleared when listeners are removed
- WeakReferences used for JavaScript interfaces
- Application context guaranteed throughout

### Graceful Degradation

Content presentation now falls back to full-screen activities when your app uses plain `Activity` classes instead of `FragmentActivity`. This ensures content is always shown.

### Complete Documentation

All public APIs now include comprehensive documentation with usage examples and thread safety notes.

## Breaking Changes

None. Version 1.1.0 is fully backward compatible with 1.0.0.

## Installation

Update your dependency to use the new version:

```groovy
dependencies {
    implementation 'com.wavecx:wavecx-android-sdk:1.1.0'
}
```

## Recommended Updates

If you're upgrading from 1.0.0, consider these improvements:

**Clear listeners in onDestroy:**
```kotlin
override fun onDestroy() {
    super.onDestroy()
    waveCx.setListener(null)
}
```

**Update modal types:**
If using mock mode, update modal type from `"bottomSheet"` to `"pageSheet"` to match API responses.

## Technical Notes

- Minimum SDK: Android 5.0 (API 21)
- Target SDK: Android 14 (API 35)
- Kotlin: 2.0.0

For detailed changes, see [CHANGELOG.md](CHANGELOG.md).
