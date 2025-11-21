# Changelog

All notable changes to the WaveCx Android SDK will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.1.0] - 2025-01-20

### Added

- **WebView Retry Logic**: Automatic retry with exponential backoff for content loading
  - Up to 3 automatic retries (500ms, 1000ms, 2000ms delays)
  - Error UI with manual retry button after automatic retries exhausted
  - Handles transient network issues gracefully
  - Applies to both Activity and BottomSheet presentation modes
- **ProGuard/R8 Rules**: Complete consumer ProGuard rules for minified builds
  - Keeps all public SDK APIs accessible
  - Preserves serialization classes (Kotlinx Serialization and Gson)
  - Protects WebView JavaScript interfaces
  - Maintains crash report line numbers
  - Tested and verified with R8 minification enabled
- **Thread Safety**: Comprehensive thread-safe implementation throughout the SDK
  - All mutable state now uses atomic classes (`AtomicBoolean`, `AtomicReference`)
  - Content cache uses `CopyOnWriteArrayList` for concurrent access
  - All listener callbacks are now guaranteed to execute on the main thread via `Handler`
  - Synchronized blocks protect compound operations
  - All public methods are now safe to call from any thread
- **API Documentation**: Complete KDoc documentation for all public APIs
  - Main `WaveCx` class with comprehensive overview and usage examples
  - All public methods with detailed descriptions, thread safety notes, and examples
  - `WaveCxListener` interface with complete callback documentation
  - Data classes: `Content`, `ModalConfig`, `CloseButtonConfig`, `WaveCxError`, `WaveCxErrorType`
  - Cross-references between related APIs
  - Ready for documentation generation with Dokka
- **Version Access**: SDK version now accessible at runtime
  - `WaveCx.VERSION` constant (e.g., "1.1.0")
  - `WaveCx.VERSION_CODE` constant (integer version code)
  - Version information in BuildConfig
- **Versioning System**: Centralized version management
  - Version defined in `gradle.properties` for single source of truth
  - Semantic versioning strategy documented
  - CHANGELOG.md for tracking changes
- **Activity Fallback**: Automatic fallback to Activity presentation when FragmentActivity unavailable
  - Ensures content is always shown regardless of Activity type
  - Graceful degradation for apps using plain Activity classes

### Changed

- **Content Cache**: Now uses thread-safe `CopyOnWriteArrayList` instead of mutable list
- **State Management**: Replaced `@Volatile` variables with atomic operations where appropriate
- **Callback Delivery**: All `WaveCxListener` callbacks now posted to main thread automatically
- **Modal Type Terminology**: Updated from "bottomSheet" to "pageSheet" to match API response format
- **Context Storage**: WaveCx now guarantees Application context storage, preventing Activity context leaks

### Fixed

- **Memory Leak Prevention**: Comprehensive memory leak fixes throughout the SDK
  - Handler callbacks now cleared when listener is removed to prevent stale callbacks
  - Comprehensive WebView cleanup in FeaturetteActivity (matching FeaturetteBottomSheet)
  - Companion instances map uses WeakReference to prevent leaks if Activities crash
  - FeaturetteBottomSheet.onDismissed callback properly cleared in onDestroyView
  - UrlChangeHandler uses WeakReference for WebView to prevent JavaScript interface leaks
  - Application context guarantee eliminates StaticFieldLeak warnings
- **Listener Lifecycle**: Enhanced setListener() documentation with lifecycle management best practices

### Improved

- **Mock Mode**: Enhanced reliability with better thread safety
- **Content Tracking**: More reliable tracking of seen/unseen content in concurrent scenarios
- **Error Handling**: Thread-safe error state management
- **Robustness**: Content presentation now works with any Activity type, not just FragmentActivity

### Technical Notes

- Minimum SDK version: Android 5.0 (API 21)
- Target SDK version: Android 14 (API 35)
- Kotlin version: 2.0.0
- All changes are backward compatible with 1.0.0

## [1.0.0] - 2024-XX-XX

### Added

- Initial public release
- Core content delivery functionality
- Trigger point system
- Popup and button-triggered content types
- Bottom sheet and full-screen presentation modes
- Mock mode for testing
- Network retry logic with exponential backoff
- Content caching per session
- User session management
- User attributes for content targeting
- Error handling and reporting
- Android Activity lifecycle integration
- WebView-based content rendering
- Customizable modal configuration (header colors, close buttons, etc.)

### Features

- `startUserSession()`: Initialize user sessions with attributes
- `endUserSession()`: Clear session data
- `triggerPoint()`: Activate content at specific points in app
- `showUserTriggeredContent()`: Manually display button-triggered content
- `hasContent()`: Check content availability
- `getTriggerPointsWithContent()`: Get all available trigger points
- `WaveCxListener`: Event callbacks for content lifecycle
- Mock mode configuration for testing without API calls
- Configurable network timeouts and retry behavior
- Debug logging mode

---

## Version History

- **1.1.0** (2025-01-20) - Thread safety, comprehensive documentation, versioning system
- **1.0.0** (2024-XX-XX) - Initial public release
