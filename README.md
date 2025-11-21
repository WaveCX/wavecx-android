# WaveCX Android SDK

The WaveCx Android SDK provides tools to integrate targeted content and user-triggered modals into your Android applications.

## Features

- Start a user session with attributes.
- Trigger content based on specific trigger points.
- Display user-triggered content modals.
- Debug mode for detailed logging of SDK operations.
- Mock mode for testing without API calls.
- Thread-safe operations throughout.

## Installation

To integrate the WaveCx Android SDK into your project, add the SDK to your dependencies.

### Gradle

Ensure the JitPack repository is added to your `build.gradle` or `settings.gradle` file:

```groovy
repositories {
    maven {
        url "https://jitpack.io"
    }
}
```

Add the WaveCX SDK dependency to your app's `build.gradle` file:

```groovy
dependencies {
    implementation 'com.wavecx:wavecx-android-sdk:1.1.0'
}
```

Ensure your `AndroidManifest.xml` includes the Internet permission:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

## Usage

### 1. Configure the SDK

Before using the SDK, initialize it with your organization's code:

```kotlin
WaveCx.initializeDefaultInstance(
    context = this,
    organizationCode = "your-organization-code"
)
```

#### Enable Debug Mode

You can enable debug mode to see detailed console logs about SDK operations:

```kotlin
WaveCx.initializeDefaultInstance(
    context = this,
    organizationCode = "your-organization-code",
    debugMode = true
)
```

When debug mode is enabled, you'll see console output like:
```
[WaveCx] WaveCx SDK initialized (org: your-org-code, apiBaseUrl: https://api.wavecx.com)
[WaveCx] Starting user session for userId: user123
[WaveCx] Fetching content from API: https://api.wavecx.com/your-org-code/targeted-content-events
[WaveCx] Content cached: 3 item(s)
[WaveCx] Trigger point called: home-screen
```

#### Enable Mock Mode

Mock mode allows you to test the SDK without making actual API calls. This is useful for local development, automated testing, and demos.

**Note:** Mock mode requires that you provide `customContent`. Use the built-in `generateMockContent()` helper to quickly create test content:

```kotlin
val mockContent = generateMockContent(listOf("home-screen", "checkout", "profile"))
val mockConfig = MockModeConfig(
    enabled = true,
    customContent = mockContent
)
WaveCx.initializeDefaultInstance(
    context = this,
    organizationCode = "your-organization-code",
    mockModeConfig = mockConfig
)
```

**With Network Delay Simulation:**

```kotlin
val mockContent = generateMockContent(listOf("home-screen", "checkout"))
val mockConfig = MockModeConfig(
    enabled = true,
    customContent = mockContent,
    networkDelay = 1500  // Simulate 1.5 second network delay
)
WaveCx.initializeDefaultInstance(
    context = this,
    organizationCode = "your-organization-code",
    debugMode = true,
    mockModeConfig = mockConfig
)
```

**Provide Custom Mock Content:**

```kotlin
val customContent = mapOf(
    "home" to listOf(
        Content(
            triggerPoint = "home",
            viewUrl = "https://example.com/custom-content",
            presentationType = "popup",
            mobileModal = ModalConfig(
                type = "overFullScreen",
                title = "Welcome!",
                headerColor = "#4A90E2",
                closeButton = CloseButtonConfig(style = "text", label = "Got it")
            )
        )
    )
)

val mockConfig = MockModeConfig(
    enabled = true,
    customContent = customContent
)
WaveCx.initializeDefaultInstance(
    context = this,
    organizationCode = "your-organization-code",
    mockModeConfig = mockConfig
)
```

When mock mode is enabled:
- No actual API calls are made
- You must provide `customContent` (use `generateMockContent()` for quick setup)
- Network delays can optionally be simulated
- Content is returned for trigger points defined in your `customContent`

### 2. Start a User Session

To start a user session, call the `startUserSession` method with the user's ID and optional attributes:

```kotlin
WaveCx.defaultInstance().startUserSession(
    userId = "userId",
    userAttributes = mapOf(
        // optional user attributes
        "your-attribute" to "attribute-value"
    )
)
```

### 3. Set Up a Listener (Optional)

You can listen to content events from the SDK by setting a listener:

```kotlin
WaveCx.defaultInstance().setListener(object : WaveCxListener {
    override fun onContentReceived(content: List<Content>) {
        // Content loaded from server
    }

    override fun onContentPresented(content: Content) {
        // Content shown to user
    }

    override fun onContentDismissed(content: Content) {
        // User dismissed content
    }

    override fun onContentChanged() {
        // Content availability changed
    }

    override fun onError(error: WaveCxError) {
        // Handle errors gracefully
        when (error.type) {
            WaveCxErrorType.NETWORK_UNAVAILABLE -> {
                // No internet connection
            }
            WaveCxErrorType.NETWORK_TIMEOUT -> {
                // Request timed out
            }
            else -> {
                // Other errors
            }
        }
    }
})
```

### 4. Trigger Content

You can trigger content based on specific trigger points. For example:

```kotlin
WaveCx.defaultInstance().triggerPoint(
    triggerPointCode = "account-page"
)
```

### 5. Check for User-Triggered Content

After triggering a point, you can check if there is user-triggered content available:

```kotlin
val hasContent = WaveCx.defaultInstance().hasContent("account-page", "button-triggered")
if (hasContent) {
    WaveCx.defaultInstance().showUserTriggeredContent("account-page")
}
```

### 6. End Session on Logout

When a user logs out, clear the session:

```kotlin
WaveCx.defaultInstance().endUserSession()
```

## Example Integration

Below is an example of how the SDK is used in a Jetpack Compose app:

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the SDK
        WaveCx.initializeDefaultInstance(
            context = this,
            organizationCode = "your-organization-code",
            debugMode = BuildConfig.DEBUG
        )

        setContent {
            MyApp()
        }
    }
}

@Composable
fun HomeScreen() {
    val waveCx = WaveCx.defaultInstance()
    var showUserTriggered by remember { mutableStateOf(false) }

    Column {
        Button(onClick = {
            // Start user session
            waveCx.startUserSession(
                userId = "userId",
                userAttributes = mapOf(
                    "your-attribute" to "attribute-value"
                )
            )
        }) {
            Text("Sign In")
        }

        Button(onClick = {
            // Trigger content
            waveCx.triggerPoint("account-page")
            showUserTriggered = waveCx.hasContent("account-page", "button-triggered")
        }) {
            Text("Account Page")
        }

        if (showUserTriggered) {
            Button(onClick = {
                waveCx.showUserTriggeredContent("account-page")
            }) {
                Text("User-Triggered Content")
            }
        }
    }
}
```

## Thread Safety

The SDK is fully thread-safe:
- All public methods can be called from any thread
- Listener callbacks are always delivered on the main thread
- Internal state uses atomic operations and synchronization

## Requirements

- Android 5.0+ (API 21)
- Kotlin 2.0.0+

## Documentation

- [CHANGELOG.md](CHANGELOG.md) - Version history and release notes
- [VERSIONING.md](VERSIONING.md) - Versioning strategy and release process

## License

This project is licensed under the MIT License. See the LICENSE file for details.
