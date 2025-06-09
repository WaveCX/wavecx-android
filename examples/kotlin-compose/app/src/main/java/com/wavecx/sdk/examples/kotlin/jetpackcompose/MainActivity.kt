package com.wavecx.sdk.examples.kotlin.jetpackcompose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.wavecx.androidsdk.WaveCx
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.wavecx.androidsdk.Content
import com.wavecx.androidsdk.WaveCxListener

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SdkExampleApp()
        }

        WaveCx.initializeDefaultInstance(
            context = this,
            organizationCode = "demos",
        )

        WaveCx.defaultInstance().setListener(object : WaveCxListener {
            override fun onContentPresented(content: Content) {
                // callback when content is presented
            }

            override fun onContentDismissed(content: Content) {
                // callback when content is dismissed
            }
        })

        WaveCx.defaultInstance().startUserSession(
            userId = "test-user",
        )
    }
}

@Composable
fun SdkExampleApp() {
    var selectedTab by remember { mutableStateOf(BottomNavItem.Home) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(selectedTab) { selectedTab = it }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            when (selectedTab) {
                BottomNavItem.Home -> HomeScreen()
                BottomNavItem.Dashboard -> DashboardScreen()
                BottomNavItem.Notifications -> NotificationsScreen()
            }
        }
    }
}

enum class BottomNavItem(val label: String) {
    Home("Home"),
    Dashboard("Dashboard"),
    Notifications("Notifications")
}

@Composable
fun BottomNavigationBar(selectedItem: BottomNavItem, onItemSelected: (BottomNavItem) -> Unit) {
    NavigationBar {
        BottomNavItem.values().forEach { item ->
            NavigationBarItem(
                selected = item == selectedItem,
                onClick = { onItemSelected(item) },
                label = { Text(item.label) },
                icon = { /* You can add icons here if you want */ }
            )
        }
    }
}

@Composable
fun HomeScreen() {
    LaunchedEffect(Unit) {
        WaveCx.defaultInstance().triggerPoint(triggerPointCode = "account-view")
    }

    CenteredText(text = "Home Screen")
    if (WaveCx.defaultInstance().hasUserTriggeredContent()) {
        Button(onClick = {WaveCx.defaultInstance().showUserTriggeredContent()}) {
            Text("User-Triggered Content")
        }
    }
}

@Composable
fun DashboardScreen() {
    CenteredText(text = "Dashboard Screen")
}

@Composable
fun NotificationsScreen() {
    CenteredText(text = "Notifications Screen")
}

@Composable
fun CenteredText(text: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = text, fontSize = 24.sp, textAlign = TextAlign.Center)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SdkExampleApp()
}