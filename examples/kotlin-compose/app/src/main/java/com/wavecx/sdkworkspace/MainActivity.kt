package com.wavecx.sdkworkspace

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.fragment.app.FragmentActivity
import com.wavecx.androidsdk.WaveCx
import com.wavecx.androidsdk.MockModeConfig
import com.wavecx.androidsdk.generateMockContent

class MainActivity : FragmentActivity() {
    private val viewModel: WaveCxViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Configure WaveCX SDK with mock mode enabled for demonstration
        val mockContent = generateMockContent(
            listOf(
                "account-dashboard",
                "low-balance-alert",
                "savings-promotion",
                "credit-card-offer",
                "investment-promotion",
                "banking-services"
            )
        )

        val mockModeConfig = MockModeConfig(
            enabled = true,
            networkDelay = 500, // Simulate 0.5 second network delay
            customContent = mockContent
        )

        WaveCx.initializeDefaultInstance(
            context = this,
            organizationCode = "demo-org",
            debugMode = true,
            mockModeConfig = mockModeConfig
        )

        // Setup listener to track analytics and content availability
        viewModel.setupListener(WaveCx.defaultInstance())

        setContent {
            MaterialTheme {
                WaveBankApp(viewModel)
            }
        }
    }
}

@Composable
fun WaveBankApp(viewModel: WaveCxViewModel) {
    if (viewModel.isSessionActive) {
        MainTabView(viewModel)
    } else {
        LoginScreen(
            viewModel = viewModel,
            onLoginSuccess = { /* Navigation handled by state change */ }
        )
    }
}

enum class BottomNavItem(
    val label: String,
    val icon: ImageVector
) {
    Accounts("Accounts", Icons.Default.AccountCircle),
    Services("Services", Icons.Default.Info),
    More("More", Icons.Default.Settings)
}

@Composable
fun MainTabView(viewModel: WaveCxViewModel) {
    var selectedTab by remember { mutableStateOf(BottomNavItem.Accounts) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                BottomNavItem.values().forEach { item ->
                    NavigationBarItem(
                        selected = item == selectedTab,
                        onClick = { selectedTab = item },
                        label = { Text(item.label) },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                BottomNavItem.Accounts -> HomeScreen(viewModel)
                BottomNavItem.Services -> ProductsScreen(viewModel)
                BottomNavItem.More -> SettingsScreen(viewModel)
            }
        }
    }
}