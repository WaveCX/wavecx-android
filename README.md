# WaveCX Android SDK

The WaveCX Android SDK provides a seamless way to integrate customer experience features into your Android application.

---

## Features Demonstrated

- **SDK Initialization**
- **Starting a User Session**
- **Using Trigger Points**
- **Showing User-Triggered Content**
- **Integrating with Jetpack Compose UI**

---

## Getting Started

### 1. Add the SDK to Your Project

Ensure the WaveCX SDK is added to your project's dependencies.

#### Maven

Ensure the WaveCX Maven repository is added to your build.gradle or settings.gradle file. 

```groovy
repositories {
    maven {
        url "https://jitpack.io"
    }
}
```

Include the WaveCX SDK dependency in your build.gradle file. Replace wavecx-sdk-version with the version of the SDK you want to use.

```groovy
dependencies {
    implementation 'com.wavecx:wavecx-android-sdk:0.0.10'
}
```

### 2. SDK Initialization

To initialize the WaveCX SDK, call `WaveCx.initializeDefaultInstance` with your `organizationCode` in your `MainActivity` or application entry point.

```kotlin
WaveCx.initializeDefaultInstance(
    context = this,
    organizationCode = "your-organization-code",
)
```

### 3. Start a User Session

Start a user session by calling `WaveCx.defaultInstance().startUserSession` with a `userId`.

```kotlin
WaveCx.defaultInstance().startUserSession(
    userId = "test-user",
)
```

---

## Example: MainActivity Integration

Below is a breakdown of how the SDK is integrated into `MainActivity.kt`:

### Full Example Code

The following example demonstrates:

- Setting up the SDK.
- Managing navigation between different screens (Home, Dashboard, Notifications).
- Using trigger points and user-triggered content.

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YourApp()
        }

        // Initialize the WaveCX SDK
        WaveCx.initializeDefaultInstance(
            context = this,
            organizationCode = "demos",
        )

        // Start a user session
        WaveCx.defaultInstance().startUserSession(
            userId = "test-user",
        )
    }
}
```

### UI Navigation and Trigger Points

The app includes a bottom navigation bar with three tabs: Home, Dashboard, and Notifications. Each tab demonstrates how you can use `WaveCx` features.

#### Home Screen Example

- A trigger point is activated when the screen is loaded.
- User-triggered content is displayed with a button.

```kotlin
@Composable
fun HomeScreen() {
    LaunchedEffect(Unit) {
        WaveCx.defaultInstance().triggerPoint(triggerPointCode = "account-view")
    }

    CenteredText(text = "Home Screen")
    if (WaveCx.defaultInstance().hasUserTriggeredContent()) {
        Button(onClick = { WaveCx.defaultInstance().showUserTriggeredContent() }) {
            Text("User-Triggered Content")
        }
    }
}
```
